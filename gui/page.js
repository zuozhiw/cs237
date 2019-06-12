//set a schedule to show my availability
//when student wants a service, confirm if tutor is available before assigning to tutor
//if no one is nearby then we can suggest to go to other building for
//add API to get more skills
//add a map and progress bar while searching

var HOST = "http://localhost:8080/api/tutor/"

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
			'url': HOST + "getTutor?email_id=" + email,
			'type': 'GET',
			'dataType': "json",
			'success': function(data) {
				t.userFound(true);
				//if not found then prompt user to be tippers user
				if(data && data.skills){
					t.skills(data.skills.split(","));
					t.available(data.available);
					t.skillsToUpdate(data.skills.split(","));
					t.visibility(true);
					t.selectedSkills([]);
					t.skillToAdd("");
				}else{
					t.available(false);
					t.visibility(false);
				}
				},
			'error': function(data){
					t.userFound(true);
			        t.editTutorMode(false);
                    t.skills([]);
                    t.available(false);
                    t.skillsToUpdate([]);
                    t.visibility(false);
                    t.selectedSkills([]);
                    t.skillToAdd("");
                    }
		});
    }).bind(this);

    this.removeTutor = (function(){
	    var t = this;
		$.ajax({
			'url': HOST + "deleteTutor",
			'type': 'DELETE',
			'data': {email_id:t.email()},
			'success': function(data) {
				t.editTutorMode(false);
				t.skills([]);
				t.available(false);
				t.skillsToUpdate([]);
				t.visibility(false);
				t.selectedSkills([]);
				t.skillToAdd("");
				t.userFound(true);
				t.successMsg("You have successfully unregistered yourself as tutor");
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
    		"email_id":this.email(),
    		"skills":this.skillsToUpdate().join(","),
    		"available":this.available()
    	}
    	var t = this;
    	var ajaxObj = {
			'url': HOST + "insertTutor",
			'type': 'POST',
			'dataType': "json",
			'data':payload,
			'error': function(data) {
				t.userFound(true);
				if(data){
					if(t.available() == true)
						t.successMsg("You have successfully updated your details");
					else
						t.successMsg("You have successfully added yourself as tutor");
					t.skills(data.skills.split(","));
					t.available(data.available);
					t.skillsToUpdate(data.skills.split(","));
					t.visibility(false);
					t.selectedSkills([]);
					t.skillToAdd("");
				}else{
					t.available(false);
					t.visibility(false);
				}
				    t.enterUser(t);
			}
		}
    	if(this.available() == true){
    		ajaxObj.url = HOST + "updateTutor";
    		//ajaxObj.type = "PUT";
    	}

		$.ajax(ajaxObj);
    }).bind(this);



    this.getDropdownValues = (function(){
    	var t = this;
    	$.ajax({
				'url': HOST + "listAllSkills",
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
				'url': HOST + "find?skill=" + this.searchSkill(),
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
