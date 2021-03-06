function send_json(json_str, pageadres) {
    // 0 - unsynchronized (replace given element)
    // 1 - synchronized (return given element)
    var mygetrequest = new ajaxRequest();
    var res = undefined;
    mygetrequest.onreadystatechange = function () {
        if (mygetrequest.readyState == 4) {
            if (mygetrequest.status == 200 || window.location.href.indexOf("http") == -1) {
                if (target_el !== undefined) {
                    document.getElementById(target_el).innerHTML = mygetrequest.responseText;
                }
                else {
                    resp = mygetrequest.responseText;
                }
            }
            else {
                alert("An error has occured making the request");
            }
        }
    };
    mygetrequest.open("POST", pageadres, true);
    mygetrequest.send(json_str);
    return(resp);
    //

}


function ajaxget(pageadres, target_el) {
    // 0 - unsynchronized (replace given element)
    // 1 - synchronized (return given element)
    var mygetrequest = new ajaxRequest();
    var res = undefined;
    var sync = target_el !== undefined;
    mygetrequest.onreadystatechange = function () {
        if (mygetrequest.readyState == 4) {
            if (mygetrequest.status == 200 || window.location.href.indexOf("http") == -1) {
                if (target_el !== undefined) {
                    document.getElementById(target_el).innerHTML = mygetrequest.responseText;
                }
                else {
                    resp = mygetrequest.responseText;
                }
            }
            else {
                alert("An error has occured making the request");
            }
        }
    };
    mygetrequest.open("GET", pageadres, sync);
    mygetrequest.send(null);
    return(resp);
    //

}

function ajaxRequest() {
    var activexmodes = ["Msxml2.XMLHTTP", "Microsoft.XMLHTTP"]; //activeX versions to check for in IE
    if (window.ActiveXObject) { //Test for support for ActiveXObject in IE first (as XMLHttpRequest in IE7 is broken)
        for (var i = 0; i < activexmodes.length; i++) {
            try {
                return new ActiveXObject(activexmodes[i]);
            }
            catch (e) {
                //suppress error
            }
        }
    }
    else if (window.XMLHttpRequest) // if Mozilla, Safari etc
        return new XMLHttpRequest();
    else
        return false;
}

function selected(ans_type) {
    if (window.quest_count === undefined)
    {
        window.quest_count =0;
    }
    //alert(ans_type);
    var el = document.getElementById('questions');
    //alert(el);

    var el1 = document.createElement('div');
    el1.setAttribute('class', 'question');
    el1.setAttribute('type', ans_type);
    var p = document.createElement('p');
    p.setAttribute('name', 'true');
    var inp = document.createElement('input');
    inp.setAttribute('type', "text");
    inp.setAttribute('size', "40");
    inp.setAttribute('class', "id1");
    inp.setAttribute('maxlength', "100");
    inp.setAttribute('placeholder', "Input question here");
    p.appendChild(inp);
    el1.appendChild(p);

   var p1 = document.createElement('p');
    p1.setAttribute('id', 'answers');
    var body = ajaxget(ans_type + '.html');
    p1.innerHTML = body;
    //alert(el1.innerHTML);


    el1.appendChild(p1);
    el.appendChild(el1);

    window.quest_count++;

    if (window.quest_count>=5){
        document.getElementById('quest_add_button').parentNode.removeChild(document.getElementById('quest_add_button'));
    }

    return false;
}

function this_login_busy()
{
    return false;
}

function check_reg() {
    alert(document.forms["regform"]["e-mail"].value);
    if (this_login_busy(document.forms["regform"]["login"].value)){
        console.log("This login is not available");
        return false;
    }

    if (document.forms["regform"]["pass"].value != document.forms["regform"]["retpass"].value) {
        alert('Passwords you`ve entered are different');
        return false;
    }

    var frm = {
        login: document.forms["regform"]["login"].value,
        password: document.forms["regform"]["pass"].value,
        email: document.forms["regform"]["e-mail"].value,
        name: document.forms["regform"]["name"].value,
        surname: document.forms["regform"]["surname"].value,
        gender: document.forms["regform"]["gender"].value
    };

    json_str = JSON.stringify(frm);
    send_json(json_str, '/rest/person/registration')

}

function logoff(){
    alert("You are logged off now");
}

function auth(){
    frm = {
        login: document.forms["auth_form"]["auth_login"].value,
        password: document.forms["auth_form"]["auth_pass"].value
    };
    json = JSON.stringify(frm);
    alert(json);

    auth1 = true;

    if (auth1 == true){
        document.getElementById('grid-container5').parentNode.removeChild(document.getElementById('grid-container5'));
        form = document.getElementById('reg_lbl');
        form.setAttribute('href', 'user_account.html');
        form.removeChild(form.childNodes[0]);
        form.textContent='My Account';

        document.getElementById('grid-container4').parentNode.removeChild(document.getElementById('grid-container4'));
        form = document.getElementById('auth_lbl');
        form.setAttribute('onclick', 'logoff()');
        form.removeChild(form.childNodes[0]);
        form.textContent='Log Out';
        return false;
    }
}

function checkForm(form)
{
    if(form.login.value == "") {
        alert("Error: Username cannot be blank!");
        form.login.focus();
        return false;
    }
    re = /^\w+$/;
    if(!re.test(form.login.value)) {
        alert("Error: Username must contain only letters, numbers and underscores!");
        form.login.focus();
        return false;
    }

    if(form.pass.value != "" && form.pass.value == form.retpass.value) {
        if(form.pass.value.length < 6) {
            alert("Error: Password must contain at least six characters!");
            form.pass.focus();
            return false;
        }
        if(form.pass.value == form.login.value) {
            alert("Error: Password must be different from Username!");
            form.pass.focus();
            return false;
        }
        re = /[0-9]/;
        if(!re.test(form.pass.value)) {
            alert("Error: password must contain at least one number (0-9)!");
            form.pass.focus();
            return false;
        }
        re = /[a-z]/;
        if(!re.test(form.pass.value)) {
            alert("Error: password must contain at least one lowercase letter (a-z)!");
            form.pwd1.focus();
            return false;
        }
        re = /[A-Z]/;
        if(!re.test(form.pass.value)) {
            alert("Error: password must contain at least one uppercase letter (A-Z)!");
            form.pwd1.focus();
            return false;
        }
    } else {
        alert("Error: Please check that you've entered and confirmed your password!");
        form.pass.focus();
        return false;
    }

    check_reg()
}
