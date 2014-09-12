var homeKit = Ti.App.iOS.homeKit; // Define HomeKit

/*
 * Window Stuff Here
 */
var win = Titanium.UI.currentWindow;
win.backgroundColor = '#FFF';
win.title = 'Manage Homes';

/*
 * Right + Button Stuff Here
 */
// create add button
var addHomeButton = Titanium.UI.createButton({
    systemButton: Ti.UI.iPhone.SystemButton.ADD
});
// add listner to button
addHomeButton.addEventListener('click', function(e){
	//Add Home Popup
	var dialog = Ti.UI.createAlertDialog({
		title: 'Enter home name',
		style: Ti.UI.iPhone.AlertDialogStyle.PLAIN_TEXT_INPUT,
		buttonNames: ['OK','Cancel']
	});
	dialog.addEventListener('click', function(e){
		if(e.index == 0) {
			homeKit.addHome(e.text);
			setTimeout(function(){
				updateHomesOnTable();
			}, 500);
		}
	});
	dialog.show();
});
// add button to view
win.rightNavButton = addHomeButton;


/*
 * ListViewStuffHere
 */
var listView = Ti.UI.createListView();

var listSection = Ti.UI.createListSection();

listView.addEventListener('noresults', function(e){
    alert("No existing homes found!");
});

listView.sections = [listSection];

win.add(listView);


homeKit.addEventListener('homeManagerDidUpdateHomes',function(managerData){
	updateHomesOnTable();
});

function updateHomesOnTable()
{	
	Ti.API.log('Existing Homes '+homeKit.homes.length);
	var data = [];
	for(var resKey in homeKit.homes){
		home = homeKit.homes[resKey];
		Ti.API.log(resKey+' - '+home.getName()+' : '+(home.primary?'Primary':''));
		data.push({
	        properties: {
	        	title: home.getName()
	        }
	    });
	}
	listSection.setItems(data);
	Ti.API.log('primaryHome: '+homeKit.primaryHome.getName());
	alert("Your primary home is "+homeKit.primaryHome.getName());
}