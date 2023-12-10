# ITMD411-finalProject


Develop an issue-tracking system tailored for IIT, leveraging a database to securely store user profiles, tickets, and associated comments.

**Login.java:** This graphical user interface handles authentication for Users and administrators. Users such as Joe User, Tom Thumb, and Peter Parker can log in, while the admin login is accessible via 'admin' credentials.

**Tickets.java:** This module allows users to create, view, delete, and modify tickets within the system.

**Dao.java:** The Data Access Object manages database connectivity and supports CRUD operations (Create, Read, Update, Delete) for tickets. It facilitates functions like inserting, updating, reading, and deleting tickets.

**ticketsJTable.java:** This component uses JTable structure and data to organize and display ticket-related information.

**TicketComment.java:** This Java class handles comments associated with tickets in the IIT trouble ticket system. It manages the interactions related to the remarks linked to individual tickets.

Users are empowered with essential capabilities, enabling them to seamlessly navigate the system by selecting tickets and effortlessly viewing their details. Additionally, they can conveniently add new tickets, fostering a streamlined process for issue reporting within the IIT trouble ticket system.

Administrators wield an enhanced level of control and responsibility. Apart from opening and closing ticket statuses, they possess the authority to create new tickets and update existing ones, including modifying ticket descriptions. Moreover, administrators hold the privilege of updating and managing comments associated with tickets, ensuring a comprehensive and efficient management of communication within the system.
