var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

server.listen(8080, function(){
	console.log("Server is now running...");
});

io.on('connection', function(socket){
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id }); //when client connects, server emit emit socketID event
	socket.emit('getPlayers', players);// on connect,getPlayers event and send array of players
	socket.broadcast.emit('newPlayer', { id: socket.id });//when client connects, broadcast the id to the other ones
	socket.on('playerMoved',function (data){
		data.id = socket.id;
		socket.broadcast.emit('playerMoved',data);
		
		console.log("playerMoved: " + "ID: " + data.id +
		"X: " +data.x+ "Y: "+ data.y  
		);
		
		
		for(var i=0;i<players.length; i++){
			if(players[i].id==data.id){
				players[i].x = data.x;
				//players[i].y = data.y;
				}
		}
	});
	
	
	socket.on('disconnect', function(){ //when it disconnect, do the following
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		for(var i = 0; i < players.length; i++){//removes player when disconnect
			if(players[i].id == socket.id){
				players.splice(i, 1);
			}
		}
	});
	players.push(new player(socket.id, 0, 0)); //when new player connects, they are pu in array.

	});

function player(id, x, y){
	this.id = id;
	this.x = x;
	this.y = y;
}