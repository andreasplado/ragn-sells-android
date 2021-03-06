package utils.carrier;

import android.content.Context;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import data.UserData;
import model.Trashcan;
import model.Trashcans;
import www.ragnsells.ee.ragnsells.R;

public class GoogleMapDirectionsQuery {
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";
    public LatLng start, wayPoint, end;
    private static final int[] COLORS = new int[]{R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryLight,R.color.colorAccent,R.color.primary_dark_material_light};

    private List<Polyline> polylines;

    public GoogleMapDirectionsQuery() {
        polylines = new ArrayList<>();
    }

    public Document getDocument() {
        ArrayList<Trashcan> trashcans = Trashcans.getTrashcans();
        Location userLocation = UserData.getUserLocation();
        String generatedUrl = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=";
        //add origin
        generatedUrl += userLocation.getLatitude() +","
                + userLocation.getLongitude()
                + "&destination=";
        //add destination
        generatedUrl += trashcans.get(trashcans.size()-1).getLatitude()
                    + ","
                    + trashcans.get(trashcans.size()-1).getLongitude();

        generatedUrl += "&waypoints=optimize:true%7C";

        //add waypoints that needs to be travelled through
        //google map allow to point out only 8 locations
        //10 with start and location
        for(int i = 0; i<trashcans.size(); i++){
            generatedUrl +=trashcans.get(i).getLatitude();
            generatedUrl +=",";
            generatedUrl +=trashcans.get(i).getLongitude();
            if(i!= trashcans.size()-1) {
                generatedUrl += "%7C";
            }
            if(i > 8){
                break;
            }
        }
        generatedUrl +="&sensor=false";


        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(generatedUrl);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDurationText (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        return node2.getTextContent();
    }

    public int getDurationValue (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        return Integer.parseInt(node2.getTextContent());
    }

    public String getDistanceText (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        return node2.getTextContent();
    }

    public int getDistanceValue (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        return Integer.parseInt(node2.getTextContent());
    }

    public String getStartAddress (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("start_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    public String getEndAddress (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("end_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    public String getCopyRights (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("copyrights");
        Node node1 = nl1.item(0);
        Log.i("CopyRights", node1.getTextContent());
        return node1.getTextContent();
    }

    public ArrayList<LatLng> getDirection (Document doc) {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for(int j = 0 ; j < arr.size() ; j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    private int getNodeIndex(NodeList nl, String nodename) {
        for(int i = 0 ; i < nl.getLength() ; i++) {
            if(nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    public void getRoudedPath(final Context context, final GoogleMap googleMap, LatLng ... latLngs){
        final LatLng start = new LatLng(18.015365, -77.499382);
        LatLng waypoint= new LatLng(18.01455, -77.499333);

        final LatLng end = new LatLng(18.012590, -77.500659);

        RoutingListener routingListener = new RoutingListener() {
            @Override
            public void onRoutingFailure(RouteException e) {
                if(e != null) {
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRoutingStart() {

            }

            @Override
            public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(start);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                googleMap.moveCamera(center);


                if(polylines.size()>0) {
                    for (Polyline poly : polylines) {
                        poly.remove();
                    }
                }

                polylines = new ArrayList<>();
                //add route(s) to the map.
                for (int j = 0; j <arrayList.size(); j++) {

                    //In case of more than 5 alternative routes
                    int colorIndex = i % COLORS.length;

                    PolylineOptions polyOptions = new PolylineOptions();
                    polyOptions.color(ContextCompat.getColor(context, COLORS[colorIndex]));
                    polyOptions.width(10 + j * 3);
                    polyOptions.addAll(arrayList.get(j).getPoints());
                    Polyline polyline = googleMap.addPolyline(polyOptions);
                    polylines.add(polyline);

                    Toast.makeText(context,"Route "+ (i+1) +": distance - "+ arrayList.get(i).getDistanceValue()+": duration - "+ arrayList.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
                }

                // Start marker
                MarkerOptions options = new MarkerOptions();
                options.position(start);
                //options.icon(VectorDrawableCompat.fromResource(R.drawable.ic_checked));
                googleMap.addMarker(options);

                // End marker
                options = new MarkerOptions();
                options.position(end);
                //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_checked));
                googleMap.addMarker(options);

            }

            @Override
            public void onRoutingCancelled() {

            }
        };
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(routingListener)
                .waypoints(start, waypoint, end)
                .build();
        routing.execute();
    }
}