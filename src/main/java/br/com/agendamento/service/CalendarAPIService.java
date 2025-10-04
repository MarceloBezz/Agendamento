package br.com.agendamento.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
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
	@Value("${TOKENS_DIRECTORY_PATH}")
	private String TOKENS_DIRECTORY_PATH;
	@Value("${CALENDAR_ID}")
	private String CALENDAR_ID;
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

	private static Credential pegarCredenciais(final NetHttpTransport HTTP_TRANSPORT,
			String credentialPath, String tokenPath) throws IOException {
		InputStream in = AgendamentoApplication.class.getResourceAsStream(credentialPath);
		if (in == null) {
			throw new FileNotFoundException("Arquivo não encontrado: " + credentialPath);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenPath)))
				.setAccessType("offline")
				.build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		return credential;
	}

	public String cadastrarEvento(LocalDateTime inicio, String nomeUsuario)
			throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, pegarCredenciais(HTTP_TRANSPORT, CREDENTIALS_FILE_PATH, TOKENS_DIRECTORY_PATH))
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
}
