INSERT INTO capability (capabilityName) VALUES
    ("Software Development"),
    ("Data Analysis"),
    ("Cybersecurity Management"),
    ("Cloud Infrastructure"),
    ("DevOps Automation")
;

INSERT INTO band (bandName) VALUES
    ("Trainee"),
    ("Associate"),
    ("Senior"),
    ("Lead"),
    ("Principal")
;

INSERT INTO status (statusName) VALUES
    ("open"),
    ("closed")
;

insert into job_roles (roleName, location, capabilityId, bandId, closingDate, description, responsibilities, sharepointUrl, numberOfOpenPositions, statusId) values
    ('Quality Assurance Engineer', 'Helsinki', 2, 1, '2024-04-12', 'In hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.', 'Cras mi pede, malesuada in, imperdiet et, commodo vulputate, justo.', 'https://cdc.gov/metus/sapien/ut/nunc/vestibulum.js', 3, 1),
    ('Penetration Tester', 'Helsinki', 4, 2, '2024-06-27', 'Nullam porttitor lacus at turpis. Donec posuere metus vitae ipsum. Aliquam non mauris.', 'Praesent id massa id nisl venenatis lacinia.', 'http://example.com/imperdiet.jsp', 1, 1),
    ('Test Automation Engineer', 'Indianapolis', 5, 4, '2024-07-07', 'In hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.', 'Vivamus tortor. Duis mattis egestas metus. Aenean fermentum.', 'http://goodreads.com/vestibulum.xml', 1, 1),
    ('DevOps Engineer', 'Indianapolis', 1, 1, '2024-01-29', 'Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.', 'Duis at velit eu est congue elementum. In hac habitasse platea dictumst.', 'https://biblegateway.com/curae/nulla/dapibus.js', 3, 1),
    ('Mobile App Developer', 'Gdańsk', 5, 5, '2024-10-03', 'Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam. Suspendisse potenti.', 'Aenean lectus.', 'https://icio.us/lobortis/vel/dapibus.jsp', 10, 1),
    ('Security Engineer', 'Atlanta', 4, 3, '2024-03-29', 'Aenean lectus. Pellentesque eget nunc. Donec quis orci eget orci vehicula condimentum.', 'Nam dui. Proin leo odio, porttitor id, consequat in, consequat ut, nulla.', 'https://deviantart.com/ornare/imperdiet.jpg', 0, 2),
    ('Test Automation Engineer', 'Copenhagen', 3, 5, '2024-08-10', 'Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat. Vestibulum sed magna at nunc commodo placerat.', 'Donec semper sapien a libero. Nam dui. Proin leo odio, porttitor id, consequat in, consequat ut, nulla.', 'http://opera.com/faucibus.json', 6, 1),
    ('Database Administrator', 'Toronto', 3, 1, '2024-12-08', 'Proin eu mi. Nulla ac enim. In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem.', 'Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa. Donec dapibus.', 'https://tamu.edu/magna/vulputate/luctus/cum.jpg', 4, 1),
    ('Data Scientist', 'Antwerp', 1, 2, '2024-11-02', 'Etiam vel augue. Vestibulum rutrum rutrum neque. Aenean auctor gravida sem.', 'Aliquam quis turpis eget elit sodales scelerisque.', 'http://noaa.gov/tortor/quis/turpis.jsp', 1, 1),
    ('Data Engineer', 'Antwerp', 3, 3, '2024-07-19', 'In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus.', 'Duis bibendum.', 'http://noaa.gov/primis.jpg', 8, 1),
    ('DevOps Engineer', 'Atlanta', 4, 5, '2024-10-21', 'Fusce consequat. Nulla nisl. Nunc nisl.', 'Donec semper sapien a libero. Nam dui.', 'http://homestead.com/id/sapien/in/sapien/iaculis/congue.jpg', 0, 2),
    ('Data Engineer', 'Helsinki', 1, 5, '2024-05-20', 'Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat.', 'Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus. Suspendisse potenti. In eleifend quam a odio.', 'http://cam.ac.uk/odio/in.js', 6, 1),
    ('Back-End Developer', 'Indianapolis', 2, 2, '2024-06-01', 'Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.', 'Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci. Nullam molestie nibh in lectus. Pellentesque at nulla.', 'https://statcounter.com/mauris/sit.html', 7, 1),
    ('Security Engineer', 'Belfast', 4, 5, '2024-07-17', 'Praesent blandit. Nam nulla. Integer pede justo, lacinia eget, tincidunt eget, tempus vel, pede.', 'Etiam pretium iaculis justo. In hac habitasse platea dictumst. Etiam faucibus cursus urna.', 'http://people.com.cn/convallis/eget.jsp', 3, 1),
    ('DevOps Engineer', 'Toronto', 3, 2, '2024-10-04', 'Praesent id massa id nisl venenatis lacinia. Aenean sit amet justo. Morbi ut odio.', 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio. Curabitur convallis.', 'https://so-net.ne.jp/nulla/ut/erat.aspx', 0, 2),
    ('Front-End Developer', 'Toronto', 1, 3, '2024-07-05', 'Praesent blandit. Nam nulla. Integer pede justo, lacinia eget, tincidunt eget, tempus vel, pede.', 'Donec posuere metus vitae ipsum. Aliquam non mauris. Morbi non lectus.', 'https://umich.edu/eget/tempus/vel/pede/morbi.js', 6, 1),
    ('Penetration Tester', 'Buenos Aires', 1, 4, '2024-01-07', 'Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh.', 'Phasellus in felis. Donec semper sapien a libero.', 'https://meetup.com/erat/vestibulum/sed/magna/at.js', 8, 1),
    ('DevOps Engineer', 'Toronto', 1, 4, '2024-12-03', 'Proin interdum mauris non ligula pellentesque ultrices. Phasellus id sapien in sapien iaculis congue. Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl.', 'Pellentesque eget nunc.', 'http://businessweek.com/platea/dictumst.xml', 0, 2),
    ('Back-End Developer', 'Toronto', 2, 4, '2024-08-24', 'Duis aliquam convallis nunc. Proin at turpis a pede posuere nonummy. Integer non velit.', 'Nulla nisl. Nunc nisl. Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.', 'https://marketwatch.com/pede/venenatis/non.xml', 7, 1),
    ('Embedded Systems Engineer', 'Atlanta', 3, 4, '2024-06-11', 'Fusce posuere felis sed lacus. Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.', 'Fusce posuere felis sed lacus. Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.', 'https://ustream.tv/molestie/hendrerit.jsp', 4, 1)
;
