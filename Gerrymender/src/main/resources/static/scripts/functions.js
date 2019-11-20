    /*Functions*/

    function onEachFeature(feature, layer) {
        var currentColor;
        layer.on("mouseover", function (e) {
            currentColor = district_color.get(feature.properties.DISTRICT);
            layer.setStyle({fillColor : "grey"});
            document.getElementById("small-info-window").style.width = "220px";
            if(sliderState == 1)
                document.getElementById("small-info-window").style.left = "370px";
            else
                document.getElementById("small-info-window").style.left = "50px";
            $(fillOutSmallWindow(feature));
        });

        layer.on("mouseout", function (e) {
            layer.setStyle({color:"black", fillColor: currentColor, weight:1, opacity: 0.8, fillOpacity: 0.5});
            //$(popup1.remove());
            document.getElementById("small-info-window").style.width = "0";
            //not working- why?
        });

        layer.on("click", function (e) {
            $(toggleInfoSlider( feature ));
        });
    }

    // active slider menu
    function toggleSlider() {
        if (sliderState === 0) { //Open slider
            document.getElementById("slide-menu").style.width = "350px";
            sliderState = 1;
            setTimeout(moveZoomControl, 0);

        } else {
            document.getElementById("slide-menu").style.width = "0";
            sliderState = 0;
            setTimeout(moveZoomControl, 200);
        }
    }

    function moveZoomControl() {
        if (sliderState === 1) { 
            container = map.zoomControl.getContainer(),
                containerTop = 350 + 'px';
            container.style.position = 'absolute';
            container.style.left = containerTop;

        } else {
            container = map.zoomControl.getContainer(),
                containerTop = 0 + 'px';
            container.style.position = 'absolute';
            container.style.left = containerTop;
        }
    }

    function toggleInfoSlider( feature ) {
        if (infoStat === feature.properties.DISTRICT) { 
            document.getElementById("slide-info").style.width = "0";
            infoStat = "null";

        } else {
            document.getElementById("slide-info").style.width = "350px";
            infoStat = feature.properties.DISTRICT;
            fillOutTable(feature);

        }
    }

    function openTab(evt, tabName) {
        // Declare all variables
        var i, tabcontent, tablinks;
      
        // Get all elements with class="tabcontent" and hide them
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
          tabcontent[i].style.display = "none";
        }
      
        // Get all elements with class="tablinks" and remove the class "active"
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
          tablinks[i].className = tablinks[i].className.replace(" active", "");
        }
      
        // Show the current tab, and add an "active" class to the button that opened the tab
        document.getElementById(tabName).style.display = "block";
        evt.currentTarget.className += " active";
      }

    function closeInfo() {
        document.getElementById("slide-info").style.width = "0";
        infoStat = "null";
    }

    function fillOutSmallWindow(feature) {
        $(document).ready(function () {
            var formData = new FormData();
            formData.append("params","nihao");
            formData.append("type",2);
            var result = $.parseJSON($.ajax({
                url: "http://localhost:8080/getSelectArea",
                type: "POST",
                data:formData,
                processData : false,
                contentType : false,
                dataType: "json",
                async: false,
                success: function (data) {
                    //console.log(data[0]);
                 },
                error: function (result) {
                    alert("error");
                  }
    }).responseText);

            // test output
            console.log(result[0]);

            $("#small-info-table tr").remove();
            var year;
            if($('#2016C').is(':checked') || $('#2016P').is(':checked'))
                year = 2016;
            else
                year = 2018;
                var items = [
                {Attr: "Name", Amout: "District "+feature.properties.DISTRICT},
                {Attr: "population", Amout: result[1]},
                {Attr: "White", Amout: "50"},
                {Attr: "Minority", Amout: "50"},
                {Attr: "Hispanic", Amout: "50"},
                {Attr: "Asian", Amout: "50"},
                {Attr: "Republican", Amout: "50"},
                {Attr: "Democratic", Amout: "50"},
                {Attr: "Year", Amout: year},
            ];
            $("#smallInfoTemplate").tmpl(items).appendTo("#small-info-table tbody");
        });
    }

    function fillOutTable(feature) {
        $(document).ready(function () {
            $("#itemList tr").remove();
            var items = [
                {Attr: "Name", Amout: "District "+feature.properties.DISTRICT},
                {Attr: "population", Amout: "50"},
                {Attr: "White", Amout: "50"},
                {Attr: "Minority", Amout: "50"},
                {Attr: "Hispanic", Amout: "50"},
                {Attr: "Asian", Amout: "50"},
                {Attr: "Republican", Amout: "50"},
                {Attr: "Democratic", Amout: "50"},
            ];
            $("#itemTemplate").tmpl(items).appendTo("#itemList tbody");
        });
    }

    function show_value(x, id){
        document.getElementById(id).innerHTML=x;
    }

    var district_color = new Map();
    district_color.set(1, '#ff6d3a');
    district_color.set(2, '#1531ff');
    district_color.set(3, '#B22222');
    district_color.set(4, '#1E90FF');
    district_color.set(5, '#FFD700');
    district_color.set(6, '#ADFF2F');
    district_color.set(7, '#4B0082');
    district_color.set(8, '#CD853F');
    district_color.set(9, '#40E0D0');
    district_color.set(10, '#FF6347');
    district_color.set(11, '#6A5ACD');
    district_color.set(12, '#0FFD70');
    district_color.set(13, '#B0C4DE');
    district_color.set(14, '#FF0000');
    district_color.set(15, '#50b2ff');
    district_color.set(16, '#5effc2');
    district_color.set(17, '#20f8ff');
    district_color.set(18, '#ffa71d');
    district_color.set(19, '#ff2fab');
    district_color.set(20, '#faffbd');
    district_color.set(21, '#cbcdff');
    district_color.set(22, '#ffb7b9');
    district_color.set(23, '#adff8f');
    district_color.set(24, '#eaff78');
    district_color.set(25, '#6fff6b');
    district_color.set(26, '#e584ff');
    district_color.set(27, '#51caff');
    district_color.set(28, '#ffe46e');
    district_color.set(29, '#57b8ff');
    district_color.set(30, '#ff71c7');
    district_color.set(31, '#90ff83');
    district_color.set(32, '#ffd48f');
    district_color.set(33, '#58c3ff');
    district_color.set(34, '#ff9de8');
    district_color.set(35, '#a7ff74');
    district_color.set(36, '#ff726f');