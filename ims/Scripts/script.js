// document.addEventListener('DOMContentLoaded', function() {
//   const user_container = document.querySelector('.user-container');
//   const registeration_btn = document.querySelector('.registeration-btn');
//   const login_btn = document.querySelector('.login-btn');

//   registeration_btn.addEventListener('click', function() {
//       user_container.classList.add('login-section--display');
//   });

//   login_btn.addEventListener('click', function() {
//       user_container.classList.remove('login-section--display');
//   });
const baseURL = SERVER_URL;

  // Function to handle registration form submission
  document.querySelector('.registeration-form-1').addEventListener('submit', function(event) {
      event.preventDefault(); // Prevent default form submission

      // Get form data
      console.log(this);
      const formData = new FormData(this);

      // document.querySelector('.registeration-form-1').addEventListener('submit', function(event) {
      //   event.preventDefault(); // Prevent default form submission
    
        // Get form data
        // const formData = new FormData(this);
    
        // Make AJAX request to register endpoint using Axios
        axios.post('http://localhost:8080/ims/api/register', formData, {
            headers: {
                'Content-Type': 'application/json' // Set Content-Type header to indicate form data
            }
        })
        .then(response => {
            if (response.status === 200) {
                // Redirect to Home page upon successful registration
                // window.location.href = 'Home.html'; // Change 'Home.html' to your actual Home page URL
                window.alert("registration successfull")
            } else {
                // Handle registration failure (e.g., display error message)
                console.error('Registration failed');
                window.alert('Registration failed');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    // });
    
      
      // Make AJAX request to register endpoint using Axios
      // axios.post('http://localhost:8080/ims/api/register', formData)
      //     .then(response => {
      //         // Check if registration was successful
      //         console.log(response);
      //         if (response.status === 200) {
      //             // Redirect to Home page upon successful registration
      //             window.location.href = 'Home.html'; // Change 'Home.html' to your actual Home page URL
      //         } else {
      //             // Handle registration failure (e.g., display error message)
      //             console.error('Registration failed');
      //         }
      //     })
      //     .catch(error => {
      //         console.error('Error:', error);
      //     });
  });

  // Function to handle login form submission
  // document.querySelector('.login-form-1').addEventListener('submit', function(event) {
  //     event.preventDefault(); // Prevent default form submission
  //   console.log(event);
  //     // Get form data
  //     console.log(this);
  //     const formData = new FormData(this);
  //     console.log(formData.entries);

  //     // Make AJAX request to login endpoint using Axios
  //     axios.post('http://localhost:8080/ims/api/login', formData, {
  //       headers:{
  //         'Content-Type':'application/json'
  //       }
  //     })
  //         .then(response => {
  //             // Check if login was successful
  //             if (response.status === 200) {
  //                 // Redirect to Home page upon successful login
  //                 window.location.href = 'Home.html'; // Change 'Home.html' to your actual Home page URL
  //             } else {
  //                 // Handle login failure (e.g., display error message)
  //                 console.error('Login failed');
  //             }
  //         })
  //         .catch(error => {
  //             console.error('Error:', error);
  //         });
  // });

  document.querySelector('.login-form-1').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default form submission



    // Get form data
    const formData = new FormData(this);

    // Make sure that the form data includes the 'username' and 'password' fields
    formData.append('username', document.getElementById('username1').value);
    formData.append('password', document.getElementById('password1').value);
    // console.log(formData);
    // Make AJAX request to login endpoint using Axios
    axios.post(`${baseURL}/api/login`, formData, 
    {
        headers:{
            'Content-Type':'application/json',
        },
        withCredentials: true
    } 
        )
        .then(res => {
            // Check if login was successful
            const {message, cookie, username, godownId, godownHeadId, role} = res.data;
            // res.cookie('cookie', cookie, { maxAge: 86400});
            const user = {username, godownId, godownHeadId, role};
            
            document.cookie = `cookie==${cookie}; path=/; SameSite=None; Secure`;
            // localStorage.setItem("cookie", cookie);
            localStorage.setItem("user", JSON.stringify(user));

            // alert("login-successful");
            Notify(message, 'success');
            // Redirect to a new page or perform other actions
            
            // window.location.href = role==="admin"? "Home.html" : "Home.html"; 
            setTimeout(() => {
              window.location.href = "Home.html";
            });
        })
        .catch(error => {
            console.error('Error:', error);
            // alert(error.response.data.message)
            Notify(error.response.data.message, 'danger');
        });
});



  