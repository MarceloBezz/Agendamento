package br.com.agendamento.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import br.com.agendamento.AgendamentoApplication;

@Service
public class CalendarAPIService {
	@Value("${APPLICATION_NAME}")
	private String APPLICATION_NAME;
	@Value("${CREDENTIALS_FILE_PATH}")
	private String CREDENTIALS_FILE_PATH;
	@Value("${CALENDAR_ID}")
	private String CALENDAR_ID;
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

	@SuppressWarnings("deprecation")
	private static Credential pegarCredenciais(final NetHttpTransport HTTP_TRANSPORT,
			String credentialPath) throws IOException {
		InputStream in = AgendamentoApplication.class.getResourceAsStream(credentialPath);
		if (in == null) {
			throw new FileNotFoundException("Arquivo não encontrado: " + credentialPath);
		}
		    GoogleCredential credential = GoogleCredential
            .fromStream(in, HTTP_TRANSPORT, JSON_FACTORY)
            .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

    	return credential;
	}

	public String cadastrarEvento(LocalDateTime inicio, String nomeUsuario)
			throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, pegarCredenciais(HTTP_TRANSPORT, CREDENTIALS_FILE_PATH))
				.setApplicationName(APPLICATION_NAME)
				.build();

		Event event = new Event()
				.setSummary("Agendamento do usuário " + nomeUsuario)
				.setLocation("Localização personalizada")
				.setDescription("Agendamento feito pelo site");

		ZoneId zoneIdInicio = ZoneId.of("America/Sao_Paulo");
		ZonedDateTime inicioZoned = inicio.atZone(zoneIdInicio);
		DateTime startDateTime = new DateTime(Date.from(inicioZoned.toInstant()));
		EventDateTime start = new EventDateTime()
				.setDateTime(startDateTime)
				.setTimeZone("America/Sao_Paulo");
		event.setStart(start);

		// Duração do agendamento: meia hora
		LocalDateTime fim = inicio.plusMinutes(30);
		ZoneId zoneIdfim = ZoneId.of("America/Sao_Paulo");
		ZonedDateTime fimZoned = fim.atZone(zoneIdfim);
		DateTime endDateTime = new DateTime(Date.from(fimZoned.toInstant()));
		EventDateTime end = new EventDateTime()
				.setDateTime(endDateTime)
				.setTimeZone("America/Sao_Paulo");
		event.setEnd(end);

		event = service.events().insert(CALENDAR_ID, event).execute();
		// System.out.printf("Evento criado: %s\n", event.getHtmlLink());
		return event.getId();
	}

	public void excluirEvento(String idEvento) throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, pegarCredenciais(HTTP_TRANSPORT, CREDENTIALS_FILE_PATH))
				.setApplicationName(APPLICATION_NAME)
				.build();

		service.events().delete(CALENDAR_ID, idEvento).execute();
	}
}
