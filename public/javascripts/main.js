var tbody = document.querySelector('tbody');

if (tbody) {
    tbody.addEventListener('click', function(e) {
        if (e.target.dataset.action === 'delete' && confirm("Do you really want to delete the note?")) {
            fetch('/notes/' + e.target.dataset.id, {
                method: 'DELETE'
            }).then(function() {
                location.reload();
            });
        }
    });
}
