var tbody = document.querySelector('tbody');

if (tbody) {
    tbody.addEventListener('click', function(e) {
        if (e.target.dataset.action === 'delete') {
            var confirmDelete = confirm("Do you want to remove the note?");

            if (confirmDelete) {
                fetch('/notes/' + e.target.dataset.id, {
                    method: 'DELETE'
                }).then(function() {
                    location.reload();
                });
            } else {
                console.log("Aborted deletion of note.");
            }
        }
    });
}