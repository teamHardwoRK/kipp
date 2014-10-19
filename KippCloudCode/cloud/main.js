
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.afterSave("BehaviorEvent", function(request) {
    var query = new Parse.Query(Parse.Installation)

    Parse.Push.send(
        {
            where: query,
            data: {
                alert: "BehaviorEvent saved."
            }
        },
        {
            success: function() {
                console.log("BehaviorEvent save push success.");
            },
            error: function() {
                console.error("BehaviorEvent save push ERROR.");
            }
        }
    );
});

Parse.Cloud.afterSave("Action", function(request) {
    var query = new Parse.Query(Parse.Installation)

    Parse.Push.send(
        {
            where: query,
            data: {
                alert: "Action saved."
            }
        },
        {
            success: function() {
                console.log("Action save push success.");
            },
            error: function() {
                console.error("Action save push ERROR.");
            }
        }
    );
});
