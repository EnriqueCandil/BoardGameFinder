document.addEventListener("DOMContentLoaded", () => {
	const navbarContainer = document.getElementById("navbar-container");  
	
  fetch("/partials/navbar.html")
    .then(res => res.text())
    .then(html => {
		navbarContainer.innerHTML = html;
		applyNavbarLogic();
    })
    .catch(err => console.error("Error cargando el navbar:", err));
});

function applyNavbarLogic () {
	const token = localStorage.getItem("token");
	
	const loginBtn = document.getElementById("loginBtn");
    const registerBtn = document.getElementById("registerBtn");
    const profileBtn = document.getElementById("profileBtn");
    const logoutBtn = document.getElementById("logoutBtn");
    const homeLink = document.getElementById("homeRedirect");
	const aboutBtn = document.getElementById("aboutBtn");
	const infoBtn = document.getElementById("infoBtn");
    
    if(token){
		if(loginBtn) loginBtn.classList.add("d-none");
		if(registerBtn) registerBtn.classList.add("d-none");
		if(profileBtn) profileBtn.classList.remove("d-none");
		if(logoutBtn) logoutBtn.classList.remove("d-none");
		
        const payload = JSON.parse(atob(token.split('.')[1]));
        const role = payload.role || null;
        
        if (homeLink) {
            homeLink.addEventListener("click", (e) => {
                e.preventDefault();
                if (role === "ADMIN") {
                    window.location.href = "/adminDashboard.html";
                } else {
                    window.location.href = "/home.html";
                }
            });
        }
        
		if (role === "ADMIN") {
			if (aboutBtn) aboutBtn.classList.add("d-none");
			if (infoBtn) infoBtn.classList.add("d-none");
			if (profileBtn) profileBtn.classList.add("d-none");
		}
		
	} else {
		if(loginBtn) loginBtn.classList.remove("d-none");
		if(registerBtn) registerBtn.classList.remove("d-none");
		if(profileBtn) profileBtn.classList.add("d-none");
		if(logoutBtn) logoutBtn.classList.add("d-none");
		
		if(homeLink){
			homeLink.addEventListener("click", (e) =>{
				e.preventDefault();
				window.location.href = "/home.html";
			});
		}
		
	}
}


function logout(){
	localStorage.removeItem("token");
	window.location.href = "/index.html";
}