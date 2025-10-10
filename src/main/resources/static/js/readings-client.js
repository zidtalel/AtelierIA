(function(){
    var thresholds = {};

    function loadThresholds() {
        return fetch('/api/config/all').then(function(r){ if (r.ok) return r.json(); return {}; }).catch(function(){ return {}; }).then(function(obj){ thresholds = obj || {}; });
    }

    function attachSensorRowClick(row) {
        row.addEventListener('click', function(){
            try {
                var id = row.id;
                var inputId = document.getElementById('temp-sensor-id');
                var inputMax = document.getElementById('temp-max');
                if (inputId) inputId.value = id;
                var val = thresholds[id];
                if (inputMax) inputMax.value = (val !== undefined ? val : '');
            } catch(e){ console.error(e); }
        });
    }

    function attachFanRowClick(row) {
        row.addEventListener('click', function(){
            try {
                var id = row.id;
                var inputId = document.getElementById('fan-id');
                var inputMin = document.getElementById('fan-min');
                if (inputId) inputId.value = id;
                var key = 'fan:' + id + ':min';
                var val = thresholds[key];
                if (inputMin) inputMin.value = (val !== undefined ? val : '');
            } catch(e){ console.error(e); }
        });
    }

    function ensureRowClickHandlers() {
        document.querySelectorAll('#sensors tbody tr').forEach(function(r){ if(!r._clickAttached){ attachSensorRowClick(r); r._clickAttached=true; }});
        document.querySelectorAll('#fans tbody tr').forEach(function(r){ if(!r._clickAttached){ attachFanRowClick(r); r._clickAttached=true; }});
    }

    function startSocket() {
        var socket = new SockJS('/ws');
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('connected', frame);
            stompClient.subscribe('/topic/readings', function(message) {
                try {
                    var readings = JSON.parse(message.body);
                    readings.forEach(function(r){
                        var row = document.getElementById(r.id);
                        var thr = thresholds[r.id];
                        if (!row) {
                            var tbody = document.querySelector('#sensors tbody');
                            row = document.createElement('tr');
                            row.id = r.id;
                            var thrText = thr !== undefined ? thr : '-';
                            var status = (r.alert || (thr !== undefined && r.valueC > thr)) ? 'ALERT' : 'OK';
                            row.innerHTML = '<td>'+r.id+'</td><td>'+r.type+'</td><td>'+r.valueC+'</td><td>'+thrText+'</td><td>'+status+'</td><td>'+r.source+'</td><td>'+r.timestamp+'</td>';
                            tbody.appendChild(row);
                            attachSensorRowClick(row);
                            row._clickAttached = true;
                        } else {
                            // columns: id=0,type=1,value=2,threshold=3,status=4,source=5,ts=6
                            row.cells[2].innerText = r.valueC;
                            var status = (r.alert || (thr !== undefined && r.valueC > thr)) ? 'ALERT' : 'OK';
                            if (row.cells[4]) row.cells[4].innerText = status;
                            if (row.cells[3]) row.cells[3].innerText = (thr !== undefined ? thr : '-');
                        }
                        if (r.alert || r.status === 'ALERT') row.classList.add('alert-row'); else row.classList.remove('alert-row');
                    });
                } catch(e) { console.error(e); }
            });

            // fan readings topic
            stompClient.subscribe('/topic/fans', function(message) {
                try {
                    var fans = JSON.parse(message.body);
                    fans.forEach(function(f){
                        var row = document.getElementById(f.id);
                        var key = 'fan:' + f.id + ':min';
                        var thr = thresholds[key];
                        if (!row) {
                            var tbody = document.querySelector('#fans tbody');
                            row = document.createElement('tr');
                            row.id = f.id;
                            var thrText = thr !== undefined ? thr : '-';
                            var status = (f.alert || (thr !== undefined && f.rpm < thr)) ? 'ALERT' : 'OK';
                            row.innerHTML = '<td>'+f.id+'</td><td>'+f.name+'</td><td>'+f.rpm+'</td><td>'+thrText+'</td><td>'+status+'</td><td>'+f.source+'</td><td>'+f.timestamp+'</td>';
                            tbody.appendChild(row);
                            attachFanRowClick(row);
                            row._clickAttached = true;
                        } else {
                            // columns: id=0,name=1,rpm=2,threshold=3,status=4,source=5,ts=6
                            row.cells[2].innerText = f.rpm;
                            var status = (f.alert || (thr !== undefined && f.rpm < thr)) ? 'ALERT' : 'OK';
                            if (row.cells[4]) row.cells[4].innerText = status;
                            if (row.cells[3]) row.cells[3].innerText = (thr !== undefined ? thr : '-');
                            if (status === 'ALERT') row.classList.add('alert-row'); else row.classList.remove('alert-row');
                        }
                    });
                } catch(e) { console.error(e); }
            });

            // alerts topic
            stompClient.subscribe('/topic/alerts', function(message) {
                try {
                    var a = JSON.parse(message.body);
                    var list = document.querySelector('#alerts ul');
                    var item = document.createElement('li');
                    // create the same structure as server-side rendered alerts
                    var ts = document.createElement('span');
                    ts.textContent = a.timestamp;
                    var sep = document.createTextNode(' \u00A0-\u00A0 ');
                    var strong = document.createElement('strong');
                    strong.textContent = a.sensorId;
                    // ensure bold even if global CSS overrides <strong>
                    strong.style.fontWeight = 'bold';
                    var colon = document.createTextNode(' : ');
                    var val = document.createElement('span');
                    val.textContent = a.value;
                    var thrText = document.createTextNode(' (seuil: ');
                    var thr = document.createElement('span');
                    thr.textContent = a.threshold;
                    var thrClose = document.createTextNode(')');

                    item.appendChild(ts);
                    item.appendChild(sep);
                    item.appendChild(strong);
                    item.appendChild(colon);
                    item.appendChild(val);
                    item.appendChild(thrText);
                    item.appendChild(thr);
                    item.appendChild(thrClose);

                    list.insertBefore(item, list.firstChild);
                } catch(e) { console.error(e); }
            });
        });
    }

    // load thresholds then start websocket and attach handlers for existing rows
    loadThresholds().then(function(){
        ensureRowClickHandlers();
        startSocket();
    });
})();
