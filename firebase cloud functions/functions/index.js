const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.userCreated = functions.database.ref("/users").onCreate( event => {
		
		const nrUsers = event.data.ref;
		nrUsers.once('value').then( result => {
			const snap = result;
			
			if(snap.numChildren() % 5 !== 0)
			return console.log("do not send notification now.");
		
		const payload = {
			notification: {
				title: "We are growing!",
				body: "Hi, pet lover! Many and many more users are registering! Ty and take care of your pets.",
				sound: "default"
			},
		};
		
		const options = {
			priority: "normal",
			timeToLive: 60 * 60 * 24
		};
		
		
		
		return admin.messaging().sendToTopic("weAreGrowingNotifications", payload, options);
		});
			
});

exports.petCreated = functions.database.ref("/pets").onWrite( event => {
		
	const newPet = event.data.val();
	console.log("da: " + JSON.stringify(newPet));
	const pet = newPet[Object.keys(newPet)[Object.keys(newPet).length-1]];
	const userId = pet.userId;
	console.log('userId ' + userId);
	
	const ref = event.data.ref;
	const root = ref.parent;
	
	const tokensRef = root.child('/tokens');
	
	tokensRef.orderByChild('userId')
		.equalTo(userId)
		.once('value').then( result => {
				
			console.log("result: " + result);	
			const payload = {
				notification: {
					title: "Last pet added name: " + JSON.stringify(pet.name),
					body: "Don't forget to take care of all your little ones.",
					sound: "default"
				},
			};
		
			console.log("trying to send notif...");
			console.log("token: " + result[Object.keys(result)[0]]);
			var vals = Object.keys(result).map(function(key) {
				return result[key];
			});
			console.log("vals: "+ JSON.stringify(vals));
			console.log("t: "+ JSON.stringify(vals[0].k.ba.key));
			return admin.messaging().sendToDevice(vals[0].k.ba.key, payload)
			.then(function(response) {
   
					console.log("Successfully sent message:", response);
			})
			.catch(function(error) {
					console.log("Error sending message:", error);
			});
		});
});