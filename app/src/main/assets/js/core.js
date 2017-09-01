function init_tags(){
    var i = document.getElementsByTagName("input");
    for (var t = 0; t < i.length; t++){i[t].className += " with-gap";}
}

function evaluateandgive(){
document.getElementById('EvaluateBtn').disabled=true;var ChoosedChoices = "X";
var allInputs = document.getElementsByTagName('input');
var allqnum = (allInputs.length - 1) / 4;
for(var i = 1; i <= allqnum; i++){
var choicesUnderQuestion = document.getElementsByName('choiceq' + i.toString());
if(choicesUnderQuestion[0].checked){ChoosedChoices = ChoosedChoices + "A";}
else if(choicesUnderQuestion[1].checked){ChoosedChoices = ChoosedChoices + "B";}
else if(choicesUnderQuestion[2].checked){ChoosedChoices = ChoosedChoices + "C";}
else if(choicesUnderQuestion[3].checked){ChoosedChoices = ChoosedChoices + "D";}
else{ChoosedChoices = ChoosedChoices + "X";}
}

window.location.href = "answers://" + ChoosedChoices;

}
var answersArray = "";
var RedColor="#ed1c24";
var GreenColor="#3bff2a";
var GrayColor ="#788990";
var answered = 0;
var xanswers = 0;
var worked = 0;

function setAnswersArray(array){
answersArray = array;init_tags();}

function evaluateforown(sub,year){
document.getElementById('EvaluateBtn').disabled=true;
var allInputs = document.getElementsByTagName('input');
var allqnum = (allInputs.length - 1) / 4;
for(var i = 1; i <= allqnum; i++){
var choicesUnderQuestion = document.getElementsByName('choiceq' + i.toString());
var ChoosedChoices = "";

if(choicesUnderQuestion[0].checked){
ChoosedChoices = ChoosedChoices + "A";}
else if(choicesUnderQuestion[1].checked){ChoosedChoices = ChoosedChoices + "B";}
else if(choicesUnderQuestion[2].checked){ChoosedChoices = ChoosedChoices + "C";}
else if(choicesUnderQuestion[3].checked){ChoosedChoices = ChoosedChoices + "D";}
else{ChoosedChoices = ChoosedChoices + "X";worked++;}
var choiceABackground = document.getElementById(sub+year+i.toString()+'a');
var choiceBBackground = document.getElementById(sub+year+i.toString()+'b');
var choiceCBackground = document.getElementById(sub+year+i.toString()+'c');
var choiceDBackground = document.getElementById(sub+year+i.toString()+'d');

if(ChoosedChoices != answersArray[i] && answersArray[i] != "X"){
switch(ChoosedChoices){
case "A":choiceABackground.style.backgroundColor=RedColor;break;
case "B":choiceBBackground.style.backgroundColor=RedColor;break;
case "C":choiceCBackground.style.backgroundColor=RedColor;break;
case "D":choiceDBackground.style.backgroundColor=RedColor;break;
case "X":
choiceABackground.style.backgroundColor=RedColor;
choiceBBackground.style.backgroundColor=RedColor;
choiceCBackground.style.backgroundColor=RedColor;
choiceDBackground.style.backgroundColor=RedColor;
break;}
}
else if(answersArray[i] == "X"){
choiceABackground.style.backgroundColor=GrayColor;
choiceBBackground.style.backgroundColor=GrayColor;
choiceCBackground.style.backgroundColor=GrayColor;
choiceDBackground.style.backgroundColor=GrayColor;
xanswers++;}
else if(ChoosedChoices == answersArray[i] && answersArray[i] != "X"){

answered++;switch(ChoosedChoices){
case "A":
choiceABackground.style.backgroundColor=GreenColor;
break;
case "B":
choiceBBackground.style.backgroundColor=GreenColor;
break;
case "C":
choiceCBackground.style.backgroundColor=GreenColor;
break;
case "D":
choiceDBackground.style.backgroundColor=GreenColor;
break;
case "X":
choiceABackground.style.backgroundColor=GreenColor;
choiceBBackground.style.backgroundColor=GreenColor;
choiceCBackground.style.backgroundColor=GreenColor;
choiceDBackground.style.backgroundColor=GreenColor;
break;}

}

}

worked = allqnum - worked;
allqnum = allqnum - xanswers;
presentMeData(allqnum , answered , worked);
}

function presentMeData(all , answered , worked){
window.location.href = "popupg12://" + all + ";" + answered + ";" + worked;
};

function changeStyle(mode){
switch (mode){
case 0:
document.getElementsByTagName('body')[0].style.backgroundColor = "#222";
document.getElementsByTagName('body')[0].style.color = "#eee";break;
case 1:document.getElementsByTagName('body')[0].style.backgroundColor = "#fae9ab";
document.getElementsByTagName('body')[0].style.color = "#000";break;
case 2:document.getElementsByTagName('body')[0].style.backgroundColor = "#0E535B";
document.getElementsByTagName('body')[0].style.color = "#fff";break;
case 3:document.getElementsByTagName('body')[0].style.backgroundColor = "#ffffff";
document.getElementsByTagName('body')[0].style.color = "#000";break;
}

};

function setUpAd(imagebase64 , title , content , link){

}

function evaluate_from_menu(){
    document.getElementById('EvaluateBtn').click();
}
