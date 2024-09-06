
const toggleButton = document.getElementById('nav-toggle');
const navLinks = document.getElementById('nav-links');

function isAdmin() {
    const role = JSON.parse(localStorage.getItem("user")).role;
    if (role === 'admin') {
        var navList = document.getElementById("nav-item");

        var li1 = document.createElement("li");
        
        li1.setAttribute("id", "godown");
        li1.setAttribute("class", "hover-link nav-item");
        li1.setAttribute("onclick", "onNavItemClick('godown', 'Godown.html')");
        li1.textContent = "Godown";

        var secondToLastItem = navList.children[navList.children.length - 1];
        navList.insertBefore(li1, secondToLastItem);

    //     var li2 = document.createElement("li");
        
    //     // Create a link element for "AddGodownHead"
    //     li2.setAttribute("id", "admin");
    //     li2.setAttribute("class", "hover-link nav-item");
    //     li2.setAttribute("onclick", "onNavItemClick('admin', 'admin.html')");
    //     li2.textContent = "Admin";

    //    var second = navList.children[navList.children.length - (navList.children.length -1)];
    //     navList.insertBefore(li2, second);

    //     var li3 = document.createElement("li");
        
    //     // Create a link element for "AddGodownHead"
    //     li3.setAttribute("id", "addgodown");
    //     li3.setAttribute("class", "hover-link nav-item");
    //     li3.setAttribute("onclick", "onNavItemClick('addgodown', 'addGodown.html')");
    //     li3.textContent = "Godown";
    
    //    var thirdLast = navList.children[navList.children.length - 2];
    //     navList.insertBefore(li3, thirdLast);

        var li4 = document.createElement("li");
        
        // Create a link element for "AddGodownHead"
        li4.setAttribute("id", "customer");
        li4.setAttribute("class", "hover-link nav-item");
        li4.setAttribute("onclick", "onNavItemClick('customer', 'Customer.html')");
        li4.textContent = "Customer";
    
       var fourthLast = navList.children[navList.children.length - 3];
        navList.insertBefore(li4, fourthLast);

        // var li3 = document.createElement("li");
        
        // // Create a link element for "AddGodownHead"
        // li3.setAttribute("id", "godown");
        // li3.setAttribute("class", "hover-link nav-item");
        // li3.setAttribute("onclick", "onNavItemClick('godown', 'Godown.html')");
        // li3.textContent = "Godown";
    }
}

function setActivePage(){
    const title = document.title.toLowerCase();
    const navItem = document.getElementById('nav-item');
    const activeNavChildren = navItem.querySelector(`#${title}`);
    activeNavChildren.classList.add('active');
}

document.addEventListener("DOMContentLoaded", ()=>{
    isAdmin();
});

toggleButton.addEventListener('click', () => {
    navLinks.classList.toggle('active')
})

function onNavItemClick(itemId, url) {
    if (url) {
        window.location.href = url;
    }
}

