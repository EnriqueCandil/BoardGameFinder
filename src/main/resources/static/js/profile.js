document.addEventListener("DOMContentLoaded",() => {
	const token = localStorage.getItem("token");
	
	if(!token){
		alert("Sesion no iniciada");
		window.location.href = "/login.html";
		return;
	}
	
    const userNameSpan = document.getElementById("userName");
    const userEmailSpan = document.getElementById("userEmail");
    const favouriteGamesList = document.getElementById("favouriteGamesList");
    const changePasswordForm = document.getElementById("changePasswordForm");
    const newPasswordInput = document.getElementById("newPassword");
    
    let currentUserId = null;
    
    // Carga de datos
    async function loadUserProfile(){
		try{
			const response = await fetch("http://localhost:8088/api/users/me",{
				method: "GET",
				headers:{
					"Content-Type": "application/json",
					"Authorization" : `Bearer ${token}`
				}
			});
			
			if(!response.ok){
				const errorText = await response.text();
				throw new Error(errorText || "Error al cargar el perfil");
			}
			
			const user = await response.json();
			currentUserId = user.idUser;
			
			userNameSpan.textContent = user.name;
			userEmailSpan.textContent = user.email;
			
			favouriteGamesList.innerHTML = "";
			
			if (user.favouriteBoardGames.length === 0) {
			    const li = document.createElement("li");
			    li.className = "list-group-item";
			    li.textContent = "No hay juegos favoritos aún";
			    favouriteGamesList.appendChild(li);
			} else {
			    user.favouriteBoardGames.forEach(game => {
			        const li = document.createElement("li");
			        li.className = "list-group-item d-flex justify-content-between align-items-center";
			
			        // Nombre clickeable
			        const nameSpan = document.createElement("span");
			        nameSpan.textContent = game.name;
			        nameSpan.style.cursor = "pointer";
			        nameSpan.classList.add("text-primary", "text-decoration-underline");
			        
			        nameSpan.addEventListener("click", () => {
			            localStorage.setItem("selectedBoardGameId", game.idBoardGame);
			            window.location.href = "/boardGameView.html";
			        });
			
			        // Botón quitar
			        const removeBtn = document.createElement("button");
			        removeBtn.className = "btn btn-sm btn-outline-danger";
			        removeBtn.textContent = "Quitar";
			        removeBtn.addEventListener("click", async (e) => {
			            e.stopPropagation();
			            const confirmRemove = confirm(`¿Quitar "${game.name}" de favoritos?`);
			            if (!confirmRemove) return;
			
			            try {
			                const response = await fetch(`http://localhost:8088/api/users/me/fav/${game.idBoardGame}`, {
			                    method: "DELETE",
			                    headers: {
			                        "Authorization": `Bearer ${token}`
			                    }
			                });
			
			                if (!response.ok) {
			                    const message = await response.text();
			                    throw new Error(message);
			                }
			
			                alert(`"${game.name}" eliminado de favoritos`);
			                li.remove();
			
			                // Mostrar mensaje si ya no hay favoritos
			                if (favouriteGamesList.children.length === 0) {
			                    const emptyLi = document.createElement("li");
			                    emptyLi.className = "list-group-item";
			                    emptyLi.textContent = "No hay juegos favoritos aún";
			                    favouriteGamesList.appendChild(emptyLi);
			                }
			
			            } catch (err) {
			                console.error(err);
			                alert("Error al quitar el juego de favoritos");
			            }
			        });
			
			        li.appendChild(nameSpan);
			        li.appendChild(removeBtn);
			        favouriteGamesList.appendChild(li);
			    });
			}
		}catch (error){
			console.error(error);
			alert("Error al cargar datos de perfil");
		}
	}
	
	// Modal para cambiar contraseña
	window.showChangePasswordModal = () => {
		newPasswordInput.value = "";
		const modal = new bootstrap.Modal(document.getElementById("changePasswordModal"));
		modal.show();
	};
	
	// Logica cambio de contraseña
	changePasswordForm.addEventListener("submit", async (e) =>{
		e.preventDefault();
		
		const newPassword = newPasswordInput.value.trim();
		
		if(newPassword.length < 6){
			alert("La contraseña debe contener al menos 6 caracteres");
			return;
		}
		
		try{
			const response = await fetch("http://localhost:8088/api/users/me",{
				method: "PUT",
				headers: {
					"Content-Type": "application/json",
					"Authorization": `Bearer ${token}`
				},
				body: JSON.stringify({ password: newPassword})
			});
			
			if(!response.ok){
				const errorText = await response.text();
				throw new Error(errorText);
			}
			
			alert("Contraseña actualizada");
			bootstrap.Modal.getInstance(document.getElementById("changePasswordModal")).hide();
		} catch (error){
			console.error(error);
			alert("Error al cambiar la contraseña");
		}
	});
	
	// Logica de eliminacion de cuenta
	window.confirmDeleteAccount = async () => {
		const confirmed = confirm("Eliminar cuenta?");
		if(!confirmed) return;
		
		try{
			const response = await fetch("http://localhost:8088/api/users/me",{
				method: "DELETE",
				headers: {
					"Authorization": `Bearer ${token}`
				}
			});
			
			if(!response.ok){
				const errorText = await response.text();
				throw new Error(errorText);
			}
			
			alert("Cuenta eliminada");
			localStorage.removeItem("token");
			window.location.href = "/index.html";
		} catch (error){
			console.error(error);
			alert("Error al eliminar cuenta");
		}
	}
	
	
	// Inicializar perfil
	loadUserProfile();
});