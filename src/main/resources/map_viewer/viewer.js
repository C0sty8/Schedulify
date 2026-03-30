var map;
var markerLayer;

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
            center: ol.proj.fromLonLat([21.2087, 45.7489]),
            zoom: 15
        })
    });
}

function showLocation(lon, lat) {
    var coords = ol.proj.fromLonLat([lon, lat]);
    markerLayer.getSource().clear();
    var markerFeature = new ol.Feature({
        geometry: new ol.geom.Point(coords)
    });
    markerLayer.getSource().addFeature(markerFeature);
    map.getView().animate({
        center: coords,
    });
}

initMap();