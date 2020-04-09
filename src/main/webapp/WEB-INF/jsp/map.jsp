<%@ page import="java.util.List" %>
<%@ page import="com.sirius.web.model.Automat" %>
<%@ page import="com.sirius.web.service.AutomatService" %>
<%@ page import="com.sirius.web.utils.AutomatClient" %>
<%@ page import="com.sirius.web.model.Location" %>
<%@ page import="java.util.ArrayList" %>
<html lang="en">
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta charset="utf-8"/>
    <link rel="apple-touch-icon" sizes="76x76" href="img/apple-icon.png">
    <link rel="icon" type="image/png" href="img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <title>
        Sirius
    </title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no'
          name='viewport'/>
    <!--     Fonts and icons     -->
    <link rel="stylesheet" type="text/css"
          href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Roboto+Slab:400,700|Material+Icons"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css">
    <!-- CSS Files -->
    <link href="css/material-dashboard.css?v=2.1.1" rel="stylesheet"/>
    <!-- CSS Just for demo purpose, don't include it in your project -->
    <link href="demo/demo.css" rel="stylesheet"/>

    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="css/util.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <!--===============================================================================================-->
    <link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.7.0/css/all.css'
          integrity='sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ' crossorigin='anonymous'>

</head>

<body>
<div class="wrapper" style="margin: 15px 50px 0px 300px">
    <div class="sidebar" data-color="green" data-background-color="white" data-image="img/sidebar-1.jpg">
        <!--
          Tip 1: You can change the color of the sidebar using: data-color="purple | azure | green | orange | danger"
          Tip 2: you can also add an image using data-image tag
      -->
        <div class="logo">
            <a href="/dashboard" class="simple-text logo-normal">
                Recycling Project
            </a>
        </div>
        <div class="sidebar-wrapper">
            <ul class="nav">
                <li class="nav-item  ">
                    <a class="nav-link" href="/dashboard">
                        <i class="material-icons">dashboard</i>
                        <p>Kontrol Paneli</p>
                    </a>
                </li>
                <li class="nav-item ">
                    <a class="nav-link" href="/insertbottle">
                        <i class="material-icons">add</i>
                        <p>Sise Ekleme</p>
                    </a>
                </li>
                <li class="nav-item active ">
                    <a class="nav-link" href="/map">
                        <i class="material-icons">map</i>
                        <p>Rota Olustur</p>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <nav class="navbar navbar-expand-lg navbar-transparent navbar-absolute fixed-top " >
        <div class="container-fluid">
            <button class="navbar-toggler" type="button" data-toggle="collapse" aria-controls="navigation-index"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="sr-only">Toggle navigation</span>
                <span class="navbar-toggler-icon icon-bar"></span>
                <span class="navbar-toggler-icon icon-bar"></span>
                <span class="navbar-toggler-icon icon-bar"></span>
            </button>

            <div class="collapse navbar-collapse justify-content-end">
                <ul class="navbar-nav" >
                    <li class="nav-item dropdown" >
                        <a class="nav-link" href="#pablo" id="navbarDropdownProfile"  data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            <i class="material-icons">person</i>
                            <p class="d-lg-none d-md-block">
                                Account
                            </p>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownProfile">
                            <a class="dropdown-item"  href="/">Log out</a>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="fixed-plugin">
        <!--   Core JS Files   -->
        <script src="js/core/jquery.min.js"></script>
        <script src="js/core/popper.min.js"></script>
        <script src="js/core/bootstrap-material-design.min.js"></script>
        <script src="js/plugins/perfect-scrollbar.jquery.min.js"></script>
        <!-- Plugin for the momentJs  -->
        <script src="js/plugins/moment.min.js"></script>
        <!--  Plugin for Sweet Alert -->
        <script src="js/plugins/sweetalert2.js"></script>
        <!-- Forms Validations Plugin -->
        <script src="js/plugins/jquery.validate.min.js"></script>
        <!-- Plugin for the Wizard, full documentation here: https://github.com/VinceG/twitter-bootstrap-wizard -->
        <script src="js/plugins/jquery.bootstrap-wizard.js"></script>
        <!--	Plugin for Select, full documentation here: http://silviomoreto.github.io/bootstrap-select -->
        <script src="js/plugins/bootstrap-selectpicker.js"></script>
        <!--  Plugin for the DateTimePicker, full documentation here: https://eonasdan.github.io/bootstrap-datetimepicker/ -->
        <script src="js/plugins/bootstrap-datetimepicker.min.js"></script>
        <!--  DataTables.net Plugin, full documentation here: https://datatables.net/  -->
        <script src="js/plugins/jquery.dataTables.min.js"></script>
        <!--	Plugin for Tags, full documentation here: https://github.com/bootstrap-tagsinput/bootstrap-tagsinputs  -->
        <script src="js/plugins/bootstrap-tagsinput.js"></script>
        <!-- Plugin for Fileupload, full documentation here: http://www.jasny.net/bootstrap/javascript/#fileinput -->
        <script src="js/plugins/jasny-bootstrap.min.js"></script>
        <!--  Full Calendar Plugin, full documentation here: https://github.com/fullcalendar/fullcalendar    -->
        <script src="js/plugins/fullcalendar.min.js"></script>
        <!-- Vector Map plugin, full documentation here: http://jvectormap.com/documentation/ -->
        <script src="js/plugins/jquery-jvectormap.js"></script>
        <!--  Plugin for the Sliders, full documentation here: http://refreshless.com/nouislider/ -->
        <script src="js/plugins/nouislider.min.js"></script>
        <!-- Include a polyfill for ES6 Promises (optional) for IE11, UC Browser and Android browser support SweetAlert -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/core-js/2.4.1/core.js"></script>
        <!-- Library for adding dinamically elements -->
        <script src="js/plugins/arrive.min.js"></script>
        <!--  Google Maps Plugin    -->
        <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
        <!-- Chartist JS -->
        <script src="js/plugins/chartist.min.js"></script>
        <!--  Notifications Plugin    -->
        <script src="js/plugins/bootstrap-notify.js"></script>
        <!-- Control Center for Material Dashboard: parallax effects, scripts for the example pages etc -->
        <script src="js/material-dashboard.js?v=2.1.1" type="text/javascript"></script>
        <!-- Material Dashboard DEMO methods, don't include it in your project! -->
        <script src="demo/demo.js"></script>
        <script>
            $(document).ready(function () {
                $().ready(function () {
                    $sidebar = $('.sidebar');
                    $sidebar_img_container = $sidebar.find('.sidebar-background');
                    $full_page = $('.full-page');
                    $sidebar_responsive = $('body > .navbar-collapse');
                    window_width = $(window).width();
                    fixed_plugin_open = $('.sidebar .sidebar-wrapper .nav li.active a p').html();
                    if (window_width > 767 && fixed_plugin_open == 'Dashboard') {
                        if ($('.fixed-plugin .dropdown').hasClass('show-dropdown')) {
                            $('.fixed-plugin .dropdown').addClass('open');
                        }
                    }
                    $('.fixed-plugin a').click(function (event) {
                        // Alex if we click on switch, stop propagation of the event, so the dropdown will not be hide, otherwise we set the  section active
                        if ($(this).hasClass('switch-trigger')) {
                            if (event.stopPropagation) {
                                event.stopPropagation();
                            } else if (window.event) {
                                window.event.cancelBubble = true;
                            }
                        }
                    });
                    $('.fixed-plugin .active-color span').click(function () {
                        $full_page_background = $('.full-page-background');
                        $(this).siblings().removeClass('active');
                        $(this).addClass('active');
                        var new_color = $(this).data('color');
                        if ($sidebar.length != 0) {
                            $sidebar.attr('data-color', new_color);
                        }
                        if ($full_page.length != 0) {
                            $full_page.attr('filter-color', new_color);
                        }
                        if ($sidebar_responsive.length != 0) {
                            $sidebar_responsive.attr('data-color', new_color);
                        }
                    });
                    $('.fixed-plugin .background-color .badge').click(function () {
                        $(this).siblings().removeClass('active');
                        $(this).addClass('active');
                        var new_color = $(this).data('background-color');
                        if ($sidebar.length != 0) {
                            $sidebar.attr('data-background-color', new_color);
                        }
                    });
                    $('.fixed-plugin .img-holder').click(function () {
                        $full_page_background = $('.full-page-background');
                        $(this).parent('li').siblings().removeClass('active');
                        $(this).parent('li').addClass('active');
                        var new_image = $(this).find("img").attr('src');
                        if ($sidebar_img_container.length != 0 && $('.switch-sidebar-image input:checked').length != 0) {
                            $sidebar_img_container.fadeOut('fast', function () {
                                $sidebar_img_container.css('background-image', 'url("' + new_image + '")');
                                $sidebar_img_container.fadeIn('fast');
                            });
                        }
                        if ($full_page_background.length != 0 && $('.switch-sidebar-image input:checked').length != 0) {
                            var new_image_full_page = $('.fixed-plugin li.active .img-holder').find('img').data('src');
                            $full_page_background.fadeOut('fast', function () {
                                $full_page_background.css('background-image', 'url("' + new_image_full_page + '")');
                                $full_page_background.fadeIn('fast');
                            });
                        }
                        if ($('.switch-sidebar-image input:checked').length == 0) {
                            var new_image = $('.fixed-plugin li.active .img-holder').find("img").attr('src');
                            var new_image_full_page = $('.fixed-plugin li.active .img-holder').find('img').data('src');
                            $sidebar_img_container.css('background-image', 'url("' + new_image + '")');
                            $full_page_background.css('background-image', 'url("' + new_image_full_page + '")');
                        }
                        if ($sidebar_responsive.length != 0) {
                            $sidebar_responsive.css('background-image', 'url("' + new_image + '")');
                        }
                    });
                    $('.switch-sidebar-image input').change(function () {
                        $full_page_background = $('.full-page-background');
                        $input = $(this);
                        if ($input.is(':checked')) {
                            if ($sidebar_img_container.length != 0) {
                                $sidebar_img_container.fadeIn('fast');
                                $sidebar.attr('data-image', '#');
                            }
                            if ($full_page_background.length != 0) {
                                $full_page_background.fadeIn('fast');
                                $full_page.attr('data-image', '#');
                            }
                            background_image = true;
                        } else {
                            if ($sidebar_img_container.length != 0) {
                                $sidebar.removeAttr('data-image');
                                $sidebar_img_container.fadeOut('fast');
                            }
                            if ($full_page_background.length != 0) {
                                $full_page.removeAttr('data-image', '#');
                                $full_page_background.fadeOut('fast');
                            }
                            background_image = false;
                        }
                    });
                    $('.switch-sidebar-mini input').change(function () {
                        $body = $('body');
                        $input = $(this);
                        if (md.misc.sidebar_mini_active == true) {
                            $('body').removeClass('sidebar-mini');
                            md.misc.sidebar_mini_active = false;
                            $('.sidebar .sidebar-wrapper, .main-panel').perfectScrollbar();
                        } else {
                            $('.sidebar .sidebar-wrapper, .main-panel').perfectScrollbar('destroy');
                            setTimeout(function () {
                                $('body').addClass('sidebar-mini');
                                md.misc.sidebar_mini_active = true;
                            }, 300);
                        }
                        // we simulate the window Resize so the charts will get updated in realtime.
                        var simulateWindowResize = setInterval(function () {
                            window.dispatchEvent(new Event('resize'));
                        }, 180);
                        // we stop the simulation of Window Resize after the animations are completed
                        setTimeout(function () {
                            clearInterval(simulateWindowResize);
                        }, 1000);
                    });
                });
            });
        </script>
        <script>
            $(document).ready(function () {
                // Javascript method's body can be found in static/js/demos.js
                md.initDashboardPageCharts();
            });
        </script>
    </div>

        <script>
            function changeSelection(){
                if (document.getElementById("myselect").value == 2)
                    document.getElementById("numVal").placeholder = "Kapasite(litre)";
                else if (document.getElementById("myselect").value == 3)
                    document.getElementById("numVal").placeholder = "Kilometre";
                else
                    document.getElementById("numVal").placeholder = "Degeri giriniz";
            }

            function rotalama() {
                if (document.getElementById("myselect").value == 1)
                    alert("Rota olusturma yontemi seciniz.");
                else if (document.getElementById("myselect").value == 2){
                    if (document.getElementById("numVal").value <= 0){
                        alert("Kapasite 0'a esit veya kucuk olamaz.");
                    }
                    else {
                        calculateAndDisplayRoute(directionsService, directionsRenderer)
                    }
                }
                else if (document.getElementById("myselect").value == 3){
                    if (document.getElementById("numVal").value <= 0){
                        alert("Rota uzunlugu 0'a esit veya kucuk olamaz.");
                    }
                    else {
                        calculateAndDisplayRoute(directionsService, directionsRenderer)
                    }
                }
            }
        </script>
        <div class="row">
            <div class="col-md-6">
                <p id="baslangic" style="font-weight:bold">Baslangic noktasi:</p>
                <p id="hedef" style="font-weight:bold">Hedef noktasi:</p>
            </div>
        </div>
    </br>
        <form action="javascript:rotalama();">
            <div class="row">
                <div class="col-md-2">
                    <select name="myselect" onchange="javascript:changeSelection();"style="border-radius: 6px;
                             height: 40px;overflow: hidden;width: 170px;
                             border-color:whitesmoke; background-color:whitesmoke;
                             font-size: smaller; color: darkgrey" id="myselect" >
                        <option value="1"> Rota olusturma yontemi</option>
                        <option value="2">Kapasiteye gore olustur</option>
                        <option value="3">Kilometreye gore olustur</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <input type="number" id="numVal" placeholder="Degeri giriniz" style="font-size: smaller;
                            height: 40px;overflow: hidden;width: 170px;
                            border-radius: 6px; border-color:whitesmoke; background-color:whitesmoke;">
                </div>
                <div class="col-md-3">
                <button type="submit" style="font-size: smaller;background-color:#4CAF50; color: white; border-radius: 6px;
                        margin-right: 0%;height: 40px;overflow: hidden;width: 170px;">Rota Olustur</button>
                </div>
            </div>
        </form>

        <div id="googleMap" style="width: 100%;height:600px;"></div>
        <script>
            var startLat;
            var startLong;

            var destinationLat;
            var destinationLong;

            var currentLat;
            var currentLong;

            var startMarker;
            var destinationMarker;
            var markers = [];
            var lastMarker;
            var map;
            var destWindow;
            var startWindow;
            var infowindow;

            var directionsService;
            var directionsRenderer;

            var waypts;

            var automatsList = [];
            var automats = [];
            var automatId;
            var latitude;
            var longitude;
            var automatsLatitude = [];
            var automatsLongitude = [];

            function deleteUnnecessaryMarkers(){
                for (var i = 0; i < markers.length; i++) {
                    if ((markers[i] != startMarker && markers[i] != destinationMarker)){
                        markers[i].setMap(null);
                    }
                }
            }

            function assignFinish() {
                destinationLat = currentLat.toFixed(3);
                destinationLong = currentLong.toFixed(3);
                destinationMarker = lastMarker;
                deleteUnnecessaryMarkers();
                infowindow.close();
                destWindow.open(map,destinationMarker);
                document.getElementById("hedef").innerHTML = "Hedef noktasi: " + destinationLat + ", " + destinationLong;
            }

            function calculateAndDisplayRoute(directionsService, directionsRenderer) {
                waypts = [];
                // get list automats
                for (var i = 0; i < 1; i++) {
                    waypts.push({
                        location: new google.maps.LatLng(39.98, 32.75),
                        stopover: true
                    });
                }
                directionsService.route(
                    {
                        origin: new google.maps.LatLng(startLat, startLong),
                        destination: new google.maps.LatLng(destinationLat, destinationLong),
                        waypoints: waypts,
                        optimizeWaypoints: true,
                        travelMode: 'DRIVING'
                    },
                    function(response, status) {
                        if (status === 'OK') {
                            directionsRenderer.setDirections(response);
                        } else {
                            window.alert('Directions request failed due to ' + status);
                        }
                    });
            }

            function assignStart() {
                startLat = currentLat.toFixed(3);
                startLong = currentLong.toFixed(3);
                startMarker = lastMarker;
                deleteUnnecessaryMarkers();
                infowindow.close();
                startWindow.open(map,startMarker);
                document.getElementById("baslangic").innerHTML = "Baslangic noktasi: " + startLat + "," + startLong;
            }

            function getLocations() {
                <%  double latitude= 39.9334; double longitude=32.8597; String automatID="";%>
                <% List<Automat> automatList = AutomatClient.listAutomats();
                 for (int i = 0; i < automatList.size(); i++) {
                    Location location = automatList.get(i).getLocation();
                    automatID = automatList.get(i).getId();
                    if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    }
                %>
                automatsList=<%= "\"" + automatList + "\""%>;
                automatId=<%= "\"" + automatID + "\""%>;
                latitude=<%= "\"" + latitude + "\""%>;
                longitude= <%= "\"" + longitude + "\""%>;
                automatsLongitude.push(longitude);
                automatsLatitude.push(latitude);
                automats.push([automatId]);

                <%} %>
            }


            function myMap() {

                directionsService = new google.maps.DirectionsService();
                directionsRenderer = new google.maps.DirectionsRenderer();
                var mapProp= {
                    center:new google.maps.LatLng(39.9334,32.8597),
                    zoom:15,
                };

                infowindow = new google.maps.InfoWindow({
                    content: "\n" +
                        "    <button onclick=\"assignStart()\"><p>baslangic noktasi olarak sec</p></button>\n" +
                        "    <p> </p>" +
                        "    <button onclick=\"assignFinish()\"><p>hedef noktasi olarak sec</p></button>\n"
                });

                startWindow =  new google.maps.InfoWindow({
                    content: "<p>Baslangic</p>"
                });

                destWindow =  new google.maps.InfoWindow({
                    content: "<p>Hedef</p>"
                });

                map = new google.maps.Map(document.getElementById("googleMap"),mapProp);
                getLocations();
                addMarkerForAutomats();
                google.maps.event.addListener(map, 'click', function(event) {
                    deleteUnnecessaryMarkers();
                    addMarker(event.latLng);
                });

                directionsRenderer.setMap(map);

                function addMarker(location) {
                    var marker = new google.maps.Marker({
                        position: location,
                        map: map,
                    });
                    currentLat = location.lat();
                    currentLong = location.lng();
                    markers.push(marker);
                    lastMarker = marker;
                    infowindow.open(map,marker);
                }
                function addMarkerForAutomats() {
                    var marker, i;

                    for(i=0; i<automats.length; i++) {
                        var marker_position = new google.maps.LatLng(automatsLatitude[i], automatsLongitude[i]);
                        marker = new google.maps.Marker({
                            position: marker_position,
                            map: map,
                            title: automats[i][0]
                        });
                    }
                }
            }
        </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyACOig7L9sTXy_HengcjO03Gq6CDWZNB2A&callback=myMap"></script>
</div>
</body>
</html>