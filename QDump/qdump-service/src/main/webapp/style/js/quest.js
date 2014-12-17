//$(document).ready(fill_text());


$(document).ready(function () {

    // if user clicked on button, the overlay layer or the dialogbox, close the dialog
    $('a.btn-ok, #dialog-overlay, #dialog-box').click(function () {
        $('#dialog-overlay, #dialog-box').hide();
        return false;
    });

    // if user resize the window, call the same function again
    // to make sure the overlay fills the screen and dialogbox aligned to center
    $(window).resize(function () {

        //only do it if the dialog box is not hidden
        if (!$('#dialog-box').is(':hidden')) popup();
    });


});


function parse(elements, offset) {
    if (offset === undefined){
        offset = 0;
    }
    ansver_coll = [];
    for (counter = offset; counter < elements.length; counter += 3) {
        answer = {};
        text = elements[counter + 1].value;
        if (text !== undefined && text !== '' && text !== null) {
            answer.answer = text;
            answer.correct = elements[counter].checked;
            ansver_coll.push(answer);
        }
    }
    return ansver_coll;
}

function get_user_id(){
    return 'adb243534affcc'
}

function create_object() {
    var quest = {};
    quest.name = document.getElementById('testname').value;
    quest.description = document.getElementById('testdescription').value;
    quest.created_by = get_user_id();
    quest.question_entities = [];

    var questions_obj = document.getElementById('questions');
    var answers_obj = null;

    for (var i = 0; i < questions_obj.children.length; i++) {
        var question = questions_obj.children[i];
        var obj_quest = {};
        var answers = [];
        quest_type = question.getAttribute('type');
        obj_quest.question_type =quest_type;

        for (m = 0; m < question.children.length; m++) {
            if (question.children[m].getAttribute('id') == 'answers') {
                answers_obj = question.children[m].children;
                switch (quest_type) {
                    case 'TEXT':
                        answer = {};
                        answer.answer = answers_obj[0].value;
                        answers.push(answer);
                        break;

                    case 'SELECT':
                        answers = parse(answers_obj,2);
                    break;

                    default:
                        answers = parse(answers_obj);
                        break;
                }
                obj_quest.answer_entities = answers;
            }
            if (question.children[m].getAttribute('name') == 'true'){
                obj_quest.question = question.children[m].children[0].value;
            }
        }
        quest.question_entities.push(obj_quest);
    }

    var json = JSON.stringify(quest);
    alert(json);
}


