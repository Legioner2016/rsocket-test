const React = require('react');
const ReactDOM = require('react-dom');
const RoutingMetadataSerializer = require('./index'); 

/* Main React application file  */

class App extends React.Component {

	constructor(props) {
		super(props);
	}
	
	componentDidMount() {
		
			//Script for jquery datatable
			
			//Rsocket imports
			const {
  				RSocketClient,
  				JsonSerializer,
  				IdentitySerializer
			} = require('rsocket-core');
			const RSocketWebSocketClient = require('rsocket-websocket-client').default;
			var client = undefined;
			
			
		 	$(document).ready(function() {
       	 	
			//Declare datatable
 			var table = $('#datatable').DataTable({
 				 "stateSave": true,
         		"processing": true,
         		"serverSide": true,
         		"lengthMenu": [5, 10, 20],
 	     		"pageLength": 10,
 	     		"searching": false,
 	     		"pagingType": "full_numbers",
     				"ajax":{
        	 				'contentType': 'application/json',
         				'url':'./imagesList',
         				'type': 'POST',
         				'data': function(d) {
         		 			for (var i = 0; i < d.columns.length; i++) {
         		        		column = d.columns[i];
         		        		delete(column.search);
       		    			}
 	               			return JSON.stringify(d);
     	       			},
         	   			'dataSrc': function (json) {
							//If we got  some data, we want ot get images for it
							let files = '';
							for (var r = 0; r < json.data.length; r++) {
								files += json.data[r].fileName + '\t'; //prepare parameter for request images
							}
							if (files != '') {
								//if we got some data we want also show the image for every row
								if (client !== undefined) {
   									 client.close(); //close previous, files list changed (another page)
								}
								//create new rsocket client
								client = new RSocketClient({
    									serializers: {
      									data: JsonSerializer,
      									metadata: IdentitySerializer 
										//metadata: RoutingMetadataSerializer
    								  },
    								  setup: {
      									// ms btw sending keepalive to server
      									keepAlive: 60000,
      									// ms timeout if no keepalive response
      									lifetime: 180000,
      									// format of `data`
      									dataMimeType: 'application/json', //i use json because cannot find good example, how to handle binary array. Binary array would be better, as needs third-fourth less data than base64 
      									// format of `metadata`
      									metadataMimeType: 'message/x.rsocket.routing.v0'
    								  },
    								  transport: new RSocketWebSocketClient({
      									url: 'ws://admin:test@localhost:6565/rsocket'
    								  }),
  									});
							
									//
									client.connect().subscribe({
    									onComplete: socket => {
      										// socket provides the rsocket interactions fire/forget, request/response,
      										// request/stream, etc as well as methods to close the socket.
      										socket.requestStream({
        										data: {
          											'fileNames': files
        										},
												//Cannot find correct way to format metadata (usually get "metadata is mailformed" from server)
												//metadata should route rsocket method and send basic security, but for now - it's a problem'
        										metadata: '' //String.fromCharCode('images.request.stream'.length) + 'images.request.stream'
      										}).subscribe({
        										onComplete: () => console.log('complete'), // Get all
        										onError: error => { //Some error
          											console.log(error);
          											addErrorMessage("Connection has been closed due to ", error);
        										},
        										onNext: payload => { //Show next image 
          											console.log(payload.data); 
          											addMessage(payload.data);
        										},
        										onSubscribe: subscription => { //some big number. As far as i know - client identifier
          											subscription.request(2147483647);
        										},
      										});
    									},
    									onError: error => { //cannot connect 
      										console.log(error);
      										addErrorMessage("Connection has been refused due to ", error);
    									},
    									onSubscribe: cancel => { 
      										/* call cancel() to abort */
    									}
  									});
							}
             	   			return json.data;
            			}
     				},
     				"columns": [
         				{"data":"id", "name":"id"},
         				{"data":"name", "name":"name"},
         				{"data":"fileName", "name":"fileName", 
								render: function(data, type, row) { 
									return '<input type="hidden" id="' + data + '">'; //stub for future replace with image
								} 
						}
     				],
     	 			columnDefs: [
 			    		{ targets: [0, 1, 2], "className": "text-center", "orderable":false, "searchable":false}
 			    	]
 	    		});



        	 })

			//If error has been happened, show it 
			function addErrorMessage(main, error) {
				$('#main').append(main + ": " + error)
			}
			
			//Show gotted image
			function addMessage(data) {
				for (const i of $('input')) {
					if (i.id == data.fileName) {
						var image = new Image();
						image.src = 'data:image/jpg;base64,' + data.data;
						image.style = "width: 50em; height: 50em";
						$(i).after(image);
					}					
				}
			}

		
	}
	
	render() {
		return (
			<div id="main">
				<table border="1px" id="datatable" className="table table-striped table-bordered" width="100%">
					<thead>
						<tr>
							<th className="text-center">Id</th>
							<th className="text-center">Name</th>
							<th className="text-center">View</th>
						</tr>
					</thead>
				</table>
			</div>
        )

		
	}
}

ReactDOM.render(
		<App />,
		document.getElementById('react')
)