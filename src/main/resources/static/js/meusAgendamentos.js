async function cancelarAgendamento(button) {
    const id = button.getAttribute("data-id")
    if (!confirm("Tem certeza de que deseja cancelar o agendamento?")) return;

    const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

    const response = await fetch(`http://localhost:8080/evento/cancelar/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        }
    });

    if (response.ok) {
        alert("Agendamento cancelado com sucesso!");
        window.location.reload();
    } else {
        alert("Erro ao cancelar agendamento!");
    }
}