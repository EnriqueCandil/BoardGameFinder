document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    const gameId = localStorage.getItem("selectedBoardGameId");

    if (!token || !gameId) {
        alert("Acceso inválido");
        window.location.href = "/home.html";
        return;
    }
    
    
    const payload = JSON.parse(atob(token.split('.')[1]));
    const role = payload.role || null;
    const backButton = document.getElementById("backButton");
    
    if(role === "ADMIN"){
		backButton.href = "adminDashboard.html";
	}else if(role === "USER") {
		backButton.href = "home.html";
	}else {
		alert("Sesion no iniciada o rol desconocido");
		localStorage.removeItem("token");
		backButton.href = "login.html";
	}

    async function loadGameDetails() {
        try {
            const response = await fetch(`http://localhost:8088/api/boardgames/${gameId}`, {
                headers: {
					"Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

            if (!response.ok) {
                const message = await response.text();
                throw new Error(message);
            }
            
            const game = await response.json();

            document.getElementById("gameName").textContent = game.name;
            document.getElementById("gamePlayers").textContent = game.numPlayers;
            document.getElementById("gameTime").textContent = game.estTime;
            document.getElementById("gameEditorial").textContent = game.editorial;
            document.getElementById("gameAge").textContent = game.age;
            document.getElementById("gameDescription").textContent = game.description;
            document.getElementById("gameCategories").textContent = game.categories.join(", ");

            const img = document.getElementById("gameImage");
            if (game.imageUrl) {
                img.src = game.imageUrl;
                img.style.display = "block";
            }
        } catch (error) {
            console.error(error);
            alert("Error cargando datos del juego");
            window.location.href = "/home.html";
        }
    }

    loadGameDetails();
});
