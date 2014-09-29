# KIPP Android App

## Team Members
* Raymond Carino [@rcarino](https://github.com/rcarino)
* Kevin Leong [@kgleong](https://github.com/kgleong)
* Hugo Nguyen [@hughhn](https://github.com/hughhn)

## User Stories

* Teachers should be able to:
    * **(REQUIRED)** log in
    * view and edit profile
        * first and last name
        * email address
        * classroom details
    * **(REQUIRED)** view a student roster
        * and filter by:
            * actions to take for the day: 
                * `celebrate` (green), `help needed` (red) students, and students who haven't been contacted in a while (yellow)
                  * **(REQUIRED)** Teacher can swipe action item to dismiss and log
                     * **(OPTIONAL)** Celebrate a student by assigning a kickboard badge
                     * **(OPTIONAL)** Help a student by logging the action taken and result
                     * **(OPTIONAL)** Call a student or a student's parent to dismiss an item from the yellow state
            * classroom
               * all students subgrouped by topic
    * **(REQUIRED)** view details for each student
        * and view the following data
            * **(REQUIRED)** basic data (name, contact info, parent contact info)
            * **(REQUIRED)** whether a student needs help
            * **(REQURIED)** current topic
            * **(REQUIRED)** metrics
                * hours spent on (effort)
                * progress (result)
                * current number of tries (effort)
            * log of actions
                * show profile information for user who created log item
            * view Kickboard characteristic data
            * analytics on how a students compares to their peers 
        * **(REQUIRED)** and perform actions on any student
            * **(REQUIRED)** reassign student topic 
            * **(REQUIRED)** call/email/sms parents/student
            * **(REQUIRED)** edit student contact info
            * add notes
                * extra time spent
            * remove a student from the `red` or `green` group.
            * log actions (e.g., calls)
            * add Kickboard characteristic metrics
                * e.g., optimism, love, social intelligence, etc.
    * display a `celebrated` students' animation
        * send personalized celebration to students (e.g., email/sms)
    * view analytics on an aggregate class/school/national level
        
* **(OPTIONAL)** Students should be able to:
    * log in 
    * edit profile
    * view progress details
    * view details for other students
    * get real time help from students or teachers:
        * real time collaboration with whiteboarding similar to google hangouts
    * request help from teacher
        * teacher can aggregate help requests
    * receive collaboration suggestions
        * other students who have done well on the current topic
            * can support inter-classroom suggestions

### Needs clarification
* Teachers should be able to:
    * view a daily agenda
        * sharing agendas with other teachers
        * initially populated from latest report 
        * and can add time slots
        * and can add/remove students from topics as needed
        * **(OPTIONAL)** create and view a seating chart
            * assign topics to seats
            * view topics by seats
