document.addEventListener("DOMContentLoaded", function() {
    const registerForm = document.getElementById("registerForm");
    const nameInput = document.getElementById("name");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    const registerError = document.getElementById("registerError");

    function isValidEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }
    function mostrarError(mensaje){
        registerError.textContent = mensaje;
        registerError.classList.remove("d-none");
        registerError.scrollIntoView({ behavior: "smooth" });
    }

    registerForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        registerError.textContent = "";

        const name = nameInput.value.trim();
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
			document.getElementById("registerError").classList.add("d-none");
			
            const response = await fetch("http://localhost:8088/api/users/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    name,
                    email,
                    password,
                    role: "USER"
                })
            });

            if (!response.ok) {
                const message = await response.text();
                throw new Error(message);
            }

            alert("Usuario creado exitosamente");
            window.location.href = "/index.html";
        } catch (err) {
			mostrarError("Error al crear usuario o usuario ya existente");
        }
    });
    
});
