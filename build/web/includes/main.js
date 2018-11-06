function validation() {
    var valid = true;
    var error_message = document.getElementById("error_message"); 
    
    cleanup();
        
    valid = checkPassword(error_message);
    valid = checkName(error_message) && valid;
    valid = checkInjection(error_message) && valid;
    valid = checkRequirements(error_message) && valid;
          
    // If there were any errors display them.
    if(!valid){
        error_message.innerHTML += "<br/>";
        error_message.className = "isVisible";
    }   
    return valid;
}

function cleanup() {
    var elementList = document.getElementsByClassName("isVisible");
    
    document.getElementById("error_message").innerHTML = "";
    
    for(var i = 0; i < elementList.length; ) {
        if(elementList[i].classList.contains("noCleanup")) {
            ++i;
            continue;
        }
        elementList[i].className = "notVisible";
    }
    
    elementList = document.getElementsByTagName("input");
    
    for(var i = 0; i < elementList.length; ++i) {
        if(elementList[i].classList.contains("button")) {
            continue;
        }
        
        elementList[i].style.backgroundColor = "white";
    }
}

function checkPassword(error_message) {
    var password = document.getElementById("password");
    var confirm_password = document.getElementById("confirm_password");
    var error_password = document.getElementById("password_error");
    var error_confirm = document.getElementById("confirm_error");
    
    if(password.value !== confirm_password.value)
    {
        password.style.backgroundColor = "yellow";
        confirm_password.style.backgroundColor = "yellow";
        
        error_password.className = "isVisible";
        error_confirm.className = "isVisible";    
        
        error_message.innerHTML += "Error! Passwords do not match!<br/>";
        
        return false;
    }
    
    return true;
}

function checkName(error_message) {
    var fullname = document.getElementById("fullname");
    var error_name = document.getElementById("fullname_error");
    
    var name = fullname.value;
    
	if (!(/\s/.test(fullname.value)) || name.charAt(name.length-1)=== " ") {
            fullname.style.backgroundColor = "yellow";
            
            error_name.className = "isVisible";                
            
            error_message.innerHTML += "Full Name is not valid!<br />";
            
            return false;
	}
        
    return true;
}

function checkInjection(error_message) {
    var quote = /'/;
    var valid = true;
    var elementList = document.getElementsByTagName("input");
    
    for(var i = 0; i < elementList.length; ++i) {
        if (quote.test(elementList[i].value)) {
            switch(elementList[i].id) {
                case "confirm_password" :
                    document.getElementById("confirm_error").className = "isVisible";
                    break;
                case "dateofbirth" :
                    document.getElementById("dateofbirth_error").className = "isVisible";
                    break;
                case "email" :
                    document.getElementById("email_error").className = "isVisible";
                    break;
                case "fullname" :
                    document.getElementById("fullname_error").className = "isVisible";
                    break;
                case "password" :
                    document.getElementById("password_error").className = "isVisible";
                    break;
                case "username" :
                    document.getElementById("username_error").className = "isVisible";
                    break;
                case "security_answer":
                    document.getElementById("security_answer_error").className = "isVisible";
                default:
                    break;
            }
            
            valid = false;
            
            elementList[i].style.backgroundColor = "yellow";
        }
    }
    
    if(!valid) {
        error_message.innerHTML += "Input has invalid characters!<br/>";
    }
    
    return valid;
}

function checkPasswordRequirement(error_message) {
    var password = document.getElementById("password");
    var error_password = document.getElementById("password_error");
    var lowerCaseLetters = /[a-z]/g;
    var upperCaseLetters = /[A-Z]/g;
    var numbers = /[0-9]/g;
    var valid = true;
    
    if (!(password.value.match(lowerCaseLetters))) {         
        error_message.innerHTML += "Password must contain a lower case letter!<br />";
        
        valid = false;
    }
    
    if (!(password.value.match(upperCaseLetters))) {               
        error_message.innerHTML += "Password must contain an upper case letter!<br />";
        
        valid = false;
    }
    
    if (!(password.value.match(numbers))) {                
        error_message.innerHTML += "Password must contain a number!<br />";
        
        valid = false;
    }
    
    if(!valid) {
        password.style.backgroundColor = "yellow";
        
        error_password.className = "isVisible";
        
        return false;
    }
    return true;
}

function DynamicForm() {
    var security_answer = document.getElementById("security_answer");  
    
    security_answer.className = "isVisible noCleanup";
}