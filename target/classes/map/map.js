var map;
var markerLayer;
var initialCenter = [21.2087, 45.7489];

//Initializeaza mapa
function initMap() {
    var osmLayer = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    markerLayer = new ol.layer.Vector({
        source: new ol.source.Vector()
    });

    map = new ol.Map({
        target: 'map',
        layers: [osmLayer, markerLayer],
        view: new ol.View({
            center: ol.proj.fromLonLat(initialCenter),
            zoom: 12
        })
    });

    map.on('click', placeMarkerAndSend);
}

//Asigura interactiunea cu utilizatorul si transforma coordonatele hartii in formatul necesar
function placeMarkerAndSend(e) {
    var coords = e.coordinate;
    var lonLat = ol.proj.toLonLat(coords);
    var lng = lonLat[0];
    var lat = lonLat[1];

    markerLayer.getSource().clear();
    var markerFeature = new ol.Feature({
        geometry: new ol.geom.Point(coords)
    });
    markerLayer.getSource().addFeature(markerFeature);
    document.title = lat + "|" + lng;
}

initMap();