const timeList = document.getElementById('timeList');
const currentDateEl = document.getElementById('currentDate');
const btnVoltarData = document.getElementById('prevDate');
const btnAvancarData = document.getElementById('nextDate');
const formCriarEvento = document.getElementById('formCriarEvento')
const inicio = document.getElementById('inicio')

let horarioDesejado;
let selectedButton = null;
let dataSelecionada = new Date();



// Formata a data em YYYY-MM-DD
const formataDate = (date) => date.toISOString().split('T')[0];

// Atualiza o texto da data
const atualizaDisplayData = () => {
  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
  currentDateEl.textContent = dataSelecionada.toLocaleDateString('pt-BR', options);
};

// Gera os horários de meia em meia hora
const geraHorarios = (horariosOcupados = []) => {
  timeList.innerHTML = '';
  const horaInicio = 8;
  const horaFim = 18;

  for (let hora = horaInicio; hora < horaFim; hora++) {
    for (let min of [0, 30]) {
      const timeStr = `${hora.toString().padStart(2, '0')}:${min.toString().padStart(2, '0')}`;
      const button = document.createElement('button');
      button.textContent = timeStr;
      button.className = 'time-btn btn btn-outline-primary m-1';
      button.type = 'button';

      // Se o horário estiver ocupado, desabilita o botão
      if (horariosOcupados.includes(timeStr)) {
        button.disabled = true;
        button.classList.add('btn-secondary');
      }

      button.addEventListener('click', () => {
        if (button.disabled) return;

        if (selectedButton && selectedButton !== button) {
          selectedButton.classList.remove('btn-primary');
          selectedButton.classList.add('btn-outline-primary');
        }

        // Marca o botão atual como selecionado
        button.classList.remove('btn-outline-primary');
        button.classList.add('btn-primary');
        selectedButton = button;

        horarioDesejado = `${formataDate(dataSelecionada)}T${button.textContent}`;
        inicio.value = horarioDesejado;
      });

      timeList.appendChild(button);
    }
  }
};

const buscaHorariosOcupados = async () => {
  const dateStr = formataDate(dataSelecionada);
  try {
    const response = await fetch(`/evento/listar?data=${dateStr}`);
    if (!response.ok) throw new Error('Erro ao buscar eventos');
    const eventos = await response.json();

    const occupied = eventos.map(e => e.substring(0, 5));
    geraHorarios(occupied);
  } catch (error) {
    console.error(error);
    geraHorarios();
  }
};

// Botões de navegação da data
btnVoltarData.addEventListener('click', () => {
  const hoje = new Date();
  hoje.setHours(0, 0, 0, 0);

  const dataComparacao = new Date(dataSelecionada);
  dataComparacao.setHours(0, 0, 0, 0);
  if (dataComparacao <= hoje) return;

  dataSelecionada.setDate(dataSelecionada.getDate() - 1);
  atualizaDisplayData();
  buscaHorariosOcupados();
});

btnAvancarData.addEventListener('click', () => {
  dataSelecionada.setDate(dataSelecionada.getDate() + 1);
  atualizaDisplayData();
  buscaHorariosOcupados();
});

formCriarEvento.addEventListener('submit', (e) => {
  e.preventDefault();

  if (horarioDesejado == null || horarioDesejado == '') {
    alert("Selecione um horário!")
    return;
  }

  formCriarEvento.submit();
})

function bloqueiaDataPassada() {
  const dataSelecionada = new Date(selectedDate);
  dataSelecionada.setHours(0, 0, 0, 0);

  btnVoltarData.disabled = dataSelecionada < hoje ? true : false;
}

// Inicializa
atualizaDisplayData();
buscaHorariosOcupados();