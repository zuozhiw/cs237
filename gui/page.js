var HOST = "http://localhost:8888"
var SENSORAHOST = "http://sensoria.ics.uci.edu:8059";
var SENSORAUCIHOST = "http://sensoria.ics.uci.edu:9001";


function sameDay(d1, d2) {
  return d1.getFullYear() === d2.getFullYear() &&
    d1.getMonth() === d2.getMonth() &&
    d1.getDate() === d2.getDate();
};



var TutorViewModel = function(data) {
	this.email = ko.observable("")
    this.skills = ko.observableArray([]);
    this.userFound = ko.observable(false);
    this.available = ko.observable(false);
    this.skillsToUpdate = ko.observableArray([]);
    this.selectedSkills = ko.observableArray([]);
    this.skillToAdd = ko.observable("");
    this.visibility = ko.observable(false);
    this.editTutorMode = ko.observable(false);
    this.successMsg = ko.observable("");
    this.searchSkill = ko.observable("");
    this.skillsDropdown = ko.observableArray([]);
    this.availableTutorsObj = ko.observable({});
    this.availableTutors =   ko.computed(function() {
        var output = [], item;
		for (var email in this.availableTutorsObj()) {
		    item = this.availableTutorsObj()[email];
		    item.email = email;
		    output.push(item);
		}
		return output;
    }, this);
    this.tutorMsg = ko.observable("");
    self = this;
    this.enterUser = function(vm){
    	this.editTutorMode(false);
    	if(document.getElementById("email").validity.valid){
    		self.loadTutorData(vm.email());	
    		self.getDropdownValues();
    	}    	
    }

    this.toggleEdit = function(vm){
    	vm.editTutorMode(true);
    	vm.successMsg("");
    }

    this.loadTutorData = (function(email){
	    var t = this;
		$.ajax({
			'url': HOST + "/index.php?getTutor=" + email,
			'type': 'GET',
			'dataType': "json",
			'success': function(data) {
				t.userFound(true);
				if(data && data.skills){
					t.skills(data.skills);
					t.available(data.available);
					t.skillsToUpdate(data.skills);
					t.visibility(data.available);
					t.selectedSkills([]);
					t.skillToAdd("");
				}else{
					t.available(false);
					t.visibility(false);
				}
			}
		});
    }).bind(this);

    this.removeTutor = (function(){
	    var t = this;
		$.ajax({
			'url': HOST + "/removeTutor.php",
			'type': 'POST',
			'data': {emailID:t.email()},
			'success': function(data) {
				t.editTutorMode(false);
				t.skills([]);
				t.available(false);
				t.skillsToUpdate([]);
				t.visibility(false);
				t.selectedSkills([]);
				t.skillToAdd("");
				t.successMsg("You have successfully deregistered yourself as tutor");
			},
			'error':function(){
				window.alert('something went wrong!');
			}
		});
    }).bind(this);


    this.addSkillToUpdate = (function(vm){
		 if ((vm.skillToAdd() != "") && (vm.skillsToUpdate().indexOf(vm.skillToAdd()) < 0))
            vm.skillsToUpdate.push(vm.skillToAdd());
        vm.skillToAdd(""); 
    }).bind(this);

    this.removeSelectedSkills = (function () {
        this.skillsToUpdate.removeAll(this.selectedSkills());
        this.selectedSkills([]); // Clear selection
    }).bind(this);

    this.addOrUpdateTutor = (function(){
    	var payload = {
    		"emailID":this.email(),
    		"available":this.visibility(),
    		"skills":this.skillsToUpdate()
    	}
    	var t = this;
    	var ajaxObj = {
			'url': HOST + "/insertTutor.php",
			'type': 'POST',
			'dataType': "json",
			'data':payload,
			'success': function(data) {
				t.userFound(true);
				if(data.skills){
					if(t.available() == true)
						t.successMsg("You have successfully updated your details");
					else
						t.successMsg("You have successfully added yourself as tutor");
					t.skills(data.skills);
					t.available(data.available);
					t.skillsToUpdate(data.skills);
					t.visibility(data.available);
					t.selectedSkills([]);
					t.skillToAdd("");
				}else{
					t.available(false);
					t.visibility(false);
				}
				t.editTutorMode(false);
			}
		}
    	if(this.available() == true){
    		ajaxObj.url = HOST + "/updateTutor.php";
    		//ajaxObj.type = "PUT";
    	}

		$.ajax(ajaxObj);
    }).bind(this);



    this.getDropdownValues = (function(){
    	var t = this;
    	$.ajax({
				'url': HOST + "/index.php?getDropdown=skill",
				'type': 'GET',
				'dataType': "json",
				'success': function(data) {
					t.skillsDropdown(data);
				},
				'error':function(){
					window.alert('something went wrong!');
				}
			});
    });


    this.findTutors = (function(){
    	var t =  this;
    	t.tutorMsg("");
    	t.availableTutorsObj({});
    	if(this.searchSkill() != ""){
    		$.ajax({
				'url': HOST + "/index.php?getAvailableTutors=" + this.searchSkill(),
				'type': 'GET',
				'dataType': "json",
				'success': function(data) {
					if(data && data.length > 0){
						data.forEach(
								function(elem) {
									t.getLocationOfUsers(elem,false);
									t.getLocationOfUsers(elem,true);
								}
						);
					}					
					// TO CALL sensora api and get nearest tutors from the list.
				},
				'error':function(){
					window.alert('something went wrong!');
				}
			});
    	}
    }).bind(this);

    this.reserveTutor = (function(vm){
    	var t = this;
    	var payload = {
    		"emailID":vm.email,
		"skill": this.searchSkill()
    	};
    	$.ajax({
			'url': HOST + '/reserveTutor.php',
			'type': 'POST',
			'dataType': "json",
			'data':payload,
			'success': function(data) {
				if(data.success == false){
					alert("Sorry! Looks like the tutor is unavailable");
				}else{
					t.tutorMsg("You have reserved "+ payload.emailID + " successfully");
					t.availableTutorsObj({});
				}				
			},
			'error':function(){
				// Do nothing.
			}
		});
    }).bind(this);

    this.getLocationOfUsers = (function(subject,deployment){
    	var t = this;
    	$.ajax({
			'url': (deployment ? SENSORAHOST : SENSORAUCIHOST) + "/semanticobservation/getLast?subject_id=" + subject +"&limit=1&region=true",
			'type': 'GET',
			'dataType': "json",
			'success': function(data) {
				if(data && data.length > 0){
					var res = data[0];
					var tutors = t.availableTutorsObj();
					var obj = res.payload;
					if(res.timestamp){
						var resDate = new Date(res.timestamp);
						var t2 = resDate.getTime();
						if(sameDay(resDate,new Date())){
							if(tutors[subject]){
								sub = t.availableTutorsObj()[subject];
								var t1 = new Date(sub.timestamp).getTime();
								if(t1 >= t2){
									obj = sub;
								}else{
									obj.timestamp = res.timestamp;
									obj.email = subject;
								}
							}else{
								obj.timestamp = res.timestamp;
								obj.email = subject;
							}
							tutors[subject]	= obj;
							t.availableTutorsObj(tutors);	
						}	
					}					
				}
			},
			'error':function(){
				// Do nothing.
			}
		});
    });
	
    if(sessionStorage.email){
    	document.getElementById('email').value = sessionStorage.email;
    	this.email(sessionStorage.email);
    	this.enterUser(this);
    }
};
 



$(function() {
    var viewModel = new TutorViewModel({});
    ko.applyBindings(viewModel);
});
