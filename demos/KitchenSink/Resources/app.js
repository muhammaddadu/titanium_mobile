// this sets the background color of the master UIView (when there are no windows/tab groups on it)
Titanium.UI.setBackgroundColor('#FFF');

var tabGroup = Titanium.UI.createTabGroup();

// manage homes
var manageHomes =  Titanium.UI.createWindow({url:'Controllers/manageHomes.js'});

var manageHomesTab = Titanium.UI.createTab({
	id:'manageHomes',
	title:'Manage Home',
	window:manageHomes
});

tabGroup.addTab(manageHomesTab);


// manage accesories
var manageAccesories =  Titanium.UI.createWindow({url:'Controllers/manageAccesories.js'});

var manageAccesoriesTab = Titanium.UI.createTab({
	id:'manageAccesories',
	title:'Manage Accesories',
	window:manageAccesories
});

tabGroup.addTab(manageAccesoriesTab);

tabGroup.open();
