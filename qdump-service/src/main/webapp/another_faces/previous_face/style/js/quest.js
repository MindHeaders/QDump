function parse(elements, offset) {
    if (offset === undefined) {
        offset = 0;
    }
    var answer_coll = [];
    for (var counter = offset; counter < elements.length; counter += 3) {
        var answer = {};
        var text = elements[counter + 1].value;
        if (text !== undefined && text !== '' && text !== null) {
            answer.answer = text;
            answer.correct = elements[counter].checked;
            answer_coll.push(answer);
        }
    }
    return answer_coll;
}

function get_user_id() {
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
        var quest_type = question.getAttribute('type');
        obj_quest.question_type = quest_type;

        for (m = 0; m < question.children.length; m++) {
            if (question.children[m].getAttribute('id') == 'answers') {
                answers_obj = question.children[m].children;
                switch (quest_type) {
                    case 'text':
                        var answer = {};
                        answer.answer = answers_obj[0].value;
                        answers.push(answer);
                        break;

                    case 'select':
                        answers = parse(answers_obj, 2);
                        break;

                    default:
                        answers = parse(answers_obj);
                        break;
                }
                obj_quest.answer_entities = answers;
            }
            if (question.children[m].getAttribute('name') == 'true') {
                obj_quest.question = question.children[m].children[0].value;
            }
        }
        quest.question_entities.push(obj_quest);
    }

    var json = JSON.stringify(quest);
    alert(json);
}


