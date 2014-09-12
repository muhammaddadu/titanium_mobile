var homeKit = Ti.App.iOS.homeKit; // Define HomeKit

/*
 * Window Stuff Here
 */
var win = Titanium.UI.currentWindow;
win.backgroundColor = '#FFF';
win.title = 'Manage Accesories';

/*
 * Right + Button Stuff Here
 */
// create add button
var addAccesoryButton = Titanium.UI.createButton({
    systemButton: Ti.UI.iPhone.SystemButton.ADD
});
// add listner to button
addAccesoryButton.addEventListener('click', function(e){
	//Titanium.UI.currentTab.open();
});
// add button to view
win.rightNavButton = addAccesoryButton;

/*
 * ListViewStuffHere
 */
var listView = Ti.UI.createListView();

var AccesorylistSection = Ti.UI.createListSection({title:'My Accesories'});

listView.addEventListener('noresults', function(e){
    alert("No Accesories Found");
});

listView.sections = [AccesorylistSection];

win.add(listView);






///
/// Add Home Win
///
/*

var addHomesWin = Ti.UI.createWindow({backgroundColor: '#FFF', fullscreen: false, title: 'Add Home',layout:'horizontal'});
addHomesWin.addEventListener('close', updateHomesOnTable);

// create add button
var addNewHomeButton = Titanium.UI.createButton({
    systemButton: Ti.UI.iPhone.SystemButton.ADD
});

// add listner to button
addNewHomeButton.addEventListener('click', addNewHome);
// add button to view
addHomesWin.rightNavButton = addNewHomeButton;


// create add button
var closeAddHomeButton = Titanium.UI.createButton({
    systemButton: Ti.UI.iPhone.SystemButton.CANCEL
});

// add listner to button
closeAddHomeButton.addEventListener('click', function(){
	addHomesWin.close();
});
// add button to view
addHomesWin.leftNavButton = closeAddHomeButton;

var addHomeInput = Ti.UI.createTextField({
  borderStyle: Ti.UI.INPUT_BORDERSTYLE_ROUNDED,
  color: '#336699',
  top: 20, left:'10%',
  width: '80%', height: 25
});

addHomesWin.add(addHomeInput);

function addNewHome(e){
	homeKit.addHome(addHomeInput.getValue());
	addHomesWin.close();
}*/
