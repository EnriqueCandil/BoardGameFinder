document.addEventListener("DOMContentLoaded", function(){

	const loginForm = document.getElementById("loginForm");
	const emailInput = document.getElementById("email");
	const passwordInput = document.getElementById("password");
	const loginError = document.getElementById("loginError");
	
	function isValidEmail(email) {
	  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	  return regex.test(email);
	}
	
  	function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  }
	
	loginForm.addEventListener("submit", async (e) => {
	  e.preventDefault();
	
	  loginError.textContent = "";
	
	  const email = emailInput.value.trim();
	  const password = passwordInput.value;
	  
      if (!email || !password) {
  		mostrarError("Por favor, complete todos los campos correctamente.");
      	return;
	  }
	
	  if (!isValidEmail(email)) {
		mostrarError("Formato de email invalido");
		return;
	  }
	
	  if (password.length < 6) {
		mostrarError("La contraseña necesita tener 6 caracteres como minimo");
		return;
	  }
	
	
	  try {
		document.getElementById("loginError").classList.add("d-none");
		
	    const response = await fetch("http://localhost:8088/api/auth/login", {
	      method: "POST",
	      headers: { "Content-Type": "application/json" },
	      body: JSON.stringify({ email, password })
	    });
	
	    if (!response.ok) {
	      const message = await response.text();
	      throw new Error(message);
	    }
	
	    const data = await response.json();
	    localStorage.setItem("token", data.token);
	    
		const decoded = parseJwt(data.token);
      	const role = decoded.role || decoded.roles || decoded.authorities || null;
	    
	    alert("Inicio de sesión exitoso");
	    
	    if(role == "ADMIN"){
			window.location.href= "/adminDashboard.html";
		}else{
			window.location.href = "/home.html";
		}
	    
	  } catch (err) {
		mostrarError("Credenciales incorrectas");
	  }
	});
	
function mostrarError(mensaje){
	const errorDiv = document.getElementById("loginError");
	errorDiv.textContent =  mensaje;
	errorDiv.classList.remove("d-none");
    errorDiv.scrollIntoView({ behavior: "smooth" });
  }

});
