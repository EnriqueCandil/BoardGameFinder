document.addEventListener("DOMContentLoaded", () => {
	const token = localStorage.getItem("token");
	
	if (!token) {
	    alert("Sesión no iniciada");
	    window.location.href = "/login.html";
	    return;
	}
	
	const tableBody = document.querySelector("#boardGamesTable tbody");
	let favouriteIds = new Set();
	
    function clearTable() {
        tableBody.innerHTML = "";
    }
    
    async function loadEnumOptions() {
		try {
		    const response = await fetch("http://localhost:8088/api/enums/all", {
		        headers: {
		            "Content-Type": "application/json",
		            "Authorization": `Bearer ${token}`
		        }
		    });
		
        	if (!response.ok) {
	            const message = await response.text();
	            throw new Error(message);
	        }
		
		    const enums = await response.json();
		
		    fillSelect("filterPlayers", enums.numPlayers);
		    fillSelect("filterTime", enums.estTime);
		    fillSelect("filterEditorial", enums.editorial);
		    fillSelect("filterAge", enums.age);
		    populateCategoryCheckboxes(enums.categories);
		
		} catch (error) {
		    console.error(error);
		    alert("Error al cargar los filtros");
		}
}

	function fillSelect(selectId, options, multiple = false) {
	    const select = document.getElementById(selectId);
	    select.innerHTML = "";
	
	    if (!multiple) {
	        const defaultOption = document.createElement("option");
	        defaultOption.value = "";
	        defaultOption.textContent = "-- Selecciona una opción --";
	        select.appendChild(defaultOption);
	    }
	
	    options.forEach(option => {
	        const opt = document.createElement("option");
	        opt.value = option;
	        opt.textContent = option;
	        select.appendChild(opt);
	    });
}

	function populateCategoryCheckboxes(categories) {
	    const container = document.getElementById("filterCategories");
	    container.innerHTML = "";
	
	    categories.forEach(cat => {
	        const div = document.createElement("div");
	        div.classList.add("form-check");
	
	        const input = document.createElement("input");
	        input.type = "checkbox";
	        input.classList.add("form-check-input");
	        input.name = "filterCategory";
	        input.value = cat;
	        input.id = `cat-${cat}`;
	
	        const label = document.createElement("label");
	        label.classList.add("form-check-label");
	        label.htmlFor = input.id;
	        label.textContent = cat;
	
	        div.appendChild(input);
	        div.appendChild(label);
	        container.appendChild(div);
	    });
}
	
	async function loadUserFavourites() {
	    try {
	        const response = await fetch("http://localhost:8088/api/users/me", {
	            headers: {
	                "Content-Type": "application/json",
	                "Authorization": `Bearer ${token}`
	            }
	        });
	
	        if (!response.ok) {
	            const message = await response.text();
	            throw new Error(message);
	        }
	
	        const user = await response.json();
	        user.favouriteBoardGames.forEach(game => favouriteIds.add(game.idBoardGame));
	    } catch (error) {
	        console.error(error);
	        alert("Error al obtener juegos favoritos del usuario");
	    }
	}
	
	
    function renderBoardGames(boardGames) {
        clearTable();
        boardGames.forEach(game => {
            const row = document.createElement("tr");

            const nameCell = document.createElement("td");
            nameCell.textContent = game.name;

            const playersCell = document.createElement("td");
            playersCell.textContent = game.numPlayers;

            const timeCell = document.createElement("td");
            timeCell.textContent = game.estTime;

            const actionCell = document.createElement("td");

            if (!favouriteIds.has(game.idBoardGame)) {
                const favButton = document.createElement("button");
                favButton.textContent = "Añadir a Favoritos";
                favButton.classList.add("btn", "btn-outline-danger", "btn-sm");

                favButton.addEventListener("click", async (e) => {
                    e.stopPropagation();
                    try {
                        const response = await fetch(`http://localhost:8088/api/users/me/fav/${game.idBoardGame}`, {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                                "Authorization": `Bearer ${token}`
                            }
                        });

                        if (!response.ok) throw new Error(await response.text());

                        alert(`"${game.name}" añadido a favoritos`);
                        favButton.remove();
                    } catch (err) {
                        console.error(err);
                        alert("Error al añadir a favoritos");
                    }
                });

                actionCell.appendChild(favButton);
            }

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
    }
	
	async function loadBoardGames() {
		clearTable();
	    try {
	        const response = await fetch("http://localhost:8088/api/boardgames/getall", {
	            method: "GET",
	            headers: {
	                "Content-Type": "application/json",
	                "Authorization": `Bearer ${token}`
	            }
	        });
	
	        if (!response.ok) {
	            const message = await response.text();
	            throw new Error(message);
	        }
	
	        const boardGames = await response.json();
			renderBoardGames(boardGames);
	       
	    } catch (error) {
	        console.error(error);
	        alert("Error al cargar los juegos");
	    }
	}
	
	
    async function applyFilters(e) {
        e.preventDefault();

        const params = new URLSearchParams();

        const name = document.getElementById("filterName").value;
        const numPlayers = document.getElementById("filterPlayers").value;
        const estTime = document.getElementById("filterTime").value;
        const editorial = document.getElementById("filterEditorial").value;
        const age = document.getElementById("filterAge").value;
        const categories = Array.from(document.querySelectorAll("input[name='filterCategory']:checked"))
			.map(cb => cb.value);

        if (name) params.append("name", name);
        if (numPlayers) params.append("numPlayers", numPlayers);
        if (estTime) params.append("estTime", estTime);
        if (editorial) params.append("editorial", editorial);
        if (age) params.append("age", age);
        categories.forEach(cat => params.append("categories", cat));

        try {
            const response = await fetch(`http://localhost:8088/api/boardgames/searchboardgames?${params.toString()}`, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

	        if (!response.ok) {
	            const message = await response.text();
	            throw new Error(message);
	        }

            const boardGames = await response.json();
            renderBoardGames(boardGames);
        } catch (error) {
            console.error(error);
            alert("Error al aplicar filtros");
        }
    }

    document.getElementById("filtersForm").addEventListener("submit", applyFilters);

    document.getElementById("toggleFilters").addEventListener("click", () => {
        const container = document.getElementById("filtersContainer");
        container.style.display = container.style.display === "none" ? "block" : "none";
    });
	
	(async () => {
		await loadEnumOptions();
	    await loadUserFavourites();
	    await loadBoardGames();
	})();
});
