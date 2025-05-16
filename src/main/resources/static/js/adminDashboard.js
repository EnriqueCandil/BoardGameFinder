document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Sesión no iniciada");
        window.location.href = "/login.html";
        return;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    const role = payload.role || null;
    
    if(role != "ADMIN"){
		alert("No ADMIN detected");
		window.location.href = "/home.html";
		return;
	}

    const tableBody = document.querySelector("#adminBoardGamesTable tbody");
    
    async function loadGames() {
        try {
            const response = await fetch("http://localhost:8088/api/boardgames/getall", {
                headers: {
					"Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });
            
			if(!response.ok){
				const errorText = await response.text();
				throw new Error(errorText || "Error al cargar la tabla");
			}

            const games = await response.json();
            tableBody.innerHTML = "";

            games.forEach(game => {
                const row = document.createElement("tr");
                
                const nameCell = document.createElement("td");
                nameCell.textContent = game.name;

                const playersCell = document.createElement("td");
                playersCell.textContent = game.numPlayers;

                const timeCell = document.createElement("td");
                timeCell.textContent = game.estTime;
                
                const actionCell = document.createElement("td");
                
        		const editBtn = document.createElement("button");
                editBtn.textContent = "Editar";
                editBtn.classList.add("btn", "btn-sm", "btn-warning", "me-2");
                editBtn.addEventListener("click", (e) => {
					e.stopPropagation();
					localStorage.setItem("editBoardGameId",game.idBoardGame);
                    window.location.href = "/boardGameFormEdit.html";
                });
                
                const deleteBtn = document.createElement("button");
                deleteBtn.textContent = "Eliminar";
                deleteBtn.classList.add("btn", "btn-sm", "btn-danger");
                deleteBtn.addEventListener("click", async (e) => {
					e.stopPropagation();
                    if (!confirm(`¿Eliminar "${game.name}"?`)) return;
                    try {
                        const res = await fetch(`http://localhost:8088/api/boardgames/delete/${game.idBoardGame}`, {
                            method: "DELETE",
                            headers: { "Authorization": `Bearer ${token}` }
                        });
                        if (!res.ok) throw new Error(await res.text());
                        loadGames();
                    } catch (err) {
                        console.error(err);
                        alert("Error al eliminar juego");
                    }
                });
                
                actionCell.appendChild(editBtn);
                actionCell.appendChild(deleteBtn);

                row.appendChild(nameCell);
                row.appendChild(playersCell);
                row.appendChild(timeCell);
                row.appendChild(actionCell);

	            row.addEventListener("click", () => {
	                localStorage.setItem("selectedBoardGameId", game.idBoardGame);
	                window.location.href = "/boardGameView.html";
	            });


                tableBody.appendChild(row);
            });
        } catch (error) {
            console.error(error);
            alert("Error al cargar juegos");
        }
    }

    loadGames();
});
