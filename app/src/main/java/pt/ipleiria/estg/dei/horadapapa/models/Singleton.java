package pt.ipleiria.estg.dei.horadapapa.models;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;
import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.isConnected;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.activities.extra.HelpTicketDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.HelpTicketListFragment;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.MenuActivity;
import pt.ipleiria.estg.dei.horadapapa.listeners.DinnersListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.FavoritesListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.InvoicesListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.ReviewsListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.TicketsListener;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;
import pt.ipleiria.estg.dei.horadapapa.utilities.JsonParser;
import pt.ipleiria.estg.dei.horadapapa.utilities.MqttHandler;

public class Singleton {
    private static RequestQueue volleyQueue = null;
    private static Singleton singleton_instance = null;
    private static DB_Helper myDatabase;
    private MqttHandler mosquitto;

    private ArrayList<Review> reviews;

    public void addReviewDB(Review r){
        myDatabase.addReviewDB(r);
    }


    public int getCurrentMealID() {
        return currentMealID;
    }

    private int currentMealID = 0; //Guarda o ID da Meal atual

    private PlatesListener platesListener = null;
    private FavoritesListener favoritesListener = null;
    private DinnersListener dinnersListener = null;
    private ReviewsListener reviewsListener = null;
    private InvoicesListener invoicesListener = null;

    private TicketsListener ticketsListener = null;


    private Singleton(Context context) {
        volleyQueue = Volley.newRequestQueue(context);
        myDatabase = new DB_Helper(context);
    }

    public static synchronized Singleton getInstance(Context context) {
        if (singleton_instance == null) {
            singleton_instance = new Singleton(context);
        }

        return singleton_instance;
    }

    public void setPlatesListener(PlatesListener platesListener) {
        this.platesListener = platesListener;
    }

    public void setInvoicesListener(InvoicesListener invoicesListener) {
        this.invoicesListener = invoicesListener;
    }
    public void setTicketsListener(TicketsListener ticketsListener) {
        this.ticketsListener = ticketsListener;
    }

    public void setFavoritesListener(FavoritesListener favoritesListener) {
        this.favoritesListener = favoritesListener;
    }

    public void setDinnersListener(DinnersListener dinnersListener) {
        this.dinnersListener = dinnersListener;
    }

    public void setReviewsListener(ReviewsListener reviewsListener) {
        this.reviewsListener = reviewsListener;
    }

    public void requestUserLogin(final Context context, User user) {

        String credentials = user.getUsername() + ":" + user.getPassword();
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Route.UserLogin(context), response -> {

                LoginInfo loginInfo = JsonParser.parseJsonLogin(response);

                if (loginInfo.getToken() == null || loginInfo.getUserId() == null) {
                    BetterToast(context, "Ocorreu um erro!");
                } else {
                    AppPreferences appPreferences = new AppPreferences(context);
                    appPreferences.setToken(loginInfo.getToken());
                    appPreferences.setUserID(loginInfo.getUserId());

                    BetterToast(context, "Login efetuado com sucesso!");

                    //TODO: Vai ter de se substituir o Intent por um Listener!
                    Intent intent = new Intent(context, MenuActivity.class);
                    intent.putExtra("username", user.getUsername());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }, error -> BetterToast(context, "Username ou Password errados!")) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + base64EncodedCredentials);
                    return headers;
                }
            };

            volleyQueue.add(stringRequest);
        }
    }

    public void requestUserRegister(final Context context, User user) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Route.UserRegister(context),
                response -> {
                    if (response.contains("success")) {
                        BetterToast(context, "Signed up!");

                        //TODO: Vai ter de se substituir o Intent por um Listener!
                        ((Activity) context).finish();
                    } else {
                        BetterToast(context, response);
                    }
                },
                error -> Route.HandleApiError(context, error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", user.getUsername());
                params.put("password", user.getPassword());
                params.put("email", user.getEmail());
                params.put("name", user.getName());
                params.put("surname", user.getSurname());
                params.put("nif", user.getNif());
                return params;
            }
        };

        Singleton.getInstance(context).volleyQueue.add(stringRequest);
    }

    public void requestPlateGetAll(Context context) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<Plate> plates = myDatabase.getPlates();

            if (platesListener != null) {
                platesListener.onRefreshPlates(plates);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.PlateGetAll(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Plate> plates = JsonParser.parseGenericList(response, Plate.class);
                    myDatabase.setPlates(plates);

                    if (platesListener != null) {
                        platesListener.onRefreshPlates(plates);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void requestMealRequests(Context context) {
        if (currentMealID == 0) {
            BetterToast(context, "refeição inválida!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.MealRequests(context, currentMealID), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<PlateRequest> requests = JsonParser.parseGenericList(response, PlateRequest.class);
                    ArrayList<Plate> plates = new ArrayList<>();

                    for (PlateRequest request : requests) {
                        int plateId = request.getPlateId();
                        Plate plate = dbGetPlate(plateId);
                        plates.add(plate);
                    }

                    if (platesListener != null) {
                        platesListener.onRefreshPlates(plates);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonArrayRequest);
        }
    }

    public Plate dbGetFavorite(int id) {
        ArrayList<Plate> items = myDatabase.getFavorites();

        for (Plate item:items) {
            if (item.getId() == id)
                return item;
        }

        return null;
    }
    public Plate dbGetPlate(int id) {
        ArrayList<Plate> items = myDatabase.getPlates();

        for (Plate item:items) {
            if (item.getId() == id)
                return item;
        }

        return null;
    }
    public Invoice dbGetInvoice(int id) {
        ArrayList<Invoice> invoices = myDatabase.getInvoices();

        for (Invoice invoice:invoices) {
            if (invoice.getId() == id)
                return invoice;
        }

        return null;
    }

    /**
     * Pede um prato
     */
    public void requestRequestPlate(Context context, int plateID, int quantity, String observation) {
        if (currentMealID == 0) {
            BetterToast(context, "refeição inválida!");
            return;
        }

        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
            return;
        }

        if (quantity == 0) {
            BetterToast(context, "quantidade inválida!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.POST,
                    Route.RequestPlate(context, currentMealID, plateID),
                    response -> {
                        Toast.makeText(context, "O pedido foi feito!", Toast.LENGTH_SHORT).show();
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    if (quantity > 0) {
                        params.put("quantity", quantity + "");
                    }
                    if (observation != null && !observation.isEmpty()) {
                        params.put("observation", observation);
                    }

                    return params;
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    public void requestMealInvoice(Context context) {
        if (currentMealID == 0) {
            BetterToast(context, "refeição inválida!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.POST,
                    Route.MealInvoice(context, currentMealID),
                    response -> {
                        Toast.makeText(context, "Fatura criada!", Toast.LENGTH_SHORT).show();
                        currentMealID = 0;

                        //Limpa a lista
                        if (platesListener != null) {
                            platesListener.onRefreshPlates(new ArrayList<>());
                        } else {
                            BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                        }

                        try {
                            mosquitto.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    public void requestInvoiceGetAll(Context context) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<Invoice> invoices = myDatabase.getInvoices();

            if (invoicesListener != null) {
                invoicesListener.onRefreshInvoices(invoices);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.Invoice(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Invoice> invoices = JsonParser.parseGenericList(response, Invoice.class);

                    if (invoices.size() == 0) {
                        BetterToast(context, "Sem faturas!");
                    }

                    myDatabase.setInvoices(invoices);

                    if (invoicesListener != null) {
                        invoicesListener.onRefreshInvoices(invoices);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonArrayRequest);
        }
    }

    /**
     * Adiciona um prato como favorito
     */
    public void requestPlateAddFavorite(Context context, int plateID) {
        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.POST,
                    Route.PlateFavorite(context, plateID),
                    response -> {
                        requestFavoritesGetAll(context);
                        BetterToast(context, "Favorito adicionado!");
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    /**
     * Remove um prato como favorito
     */
    public void requestPlateRemoveFavorite(Context context, int plateID) {
        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.DELETE,
                    Route.PlateFavorite(context, plateID),
                    response -> {
                        requestFavoritesGetAll(context);
                        BetterToast(context, "Favorito removido!");
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    public void requestFavoritesGetAll(Context context) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<Plate> plates = myDatabase.getFavorites();

            if (favoritesListener != null) {
                favoritesListener.onRefreshFavorites(plates);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.PlateFavorite(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Favorite> favorites = JsonParser.parseGenericList(response, Favorite.class);

                    myDatabase.setFavorites(favorites);

                    if (favorites.size() > 0)
                    {
                        ArrayList<Plate> plates = myDatabase.getFavorites();

                        if (plates.size() > 0) {
                            if (favoritesListener != null) {
                                favoritesListener.onRefreshFavorites(plates);
                            }
                        }
                        else
                        {
                            BetterToast(context, "Os pratos ainda não foram carregados!");
                        }
                    }
                    else
                    {
                        BetterToast(context, "Sem favoritos!");
                    }
                }
            }, error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void requestDinnerGetAll(Context context) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.Dinner(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Dinner> dinners = JsonParser.parseGenericList(response, Dinner.class);

                    if (dinners != null) {
                        dinnersListener.onRefreshDinners(dinners);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void requestDinnerStart(Context context, int dinnerID) {
        if (dinnerID == 0) {
            BetterToast(context, "Mesa inválida!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Route.Dinner(context, dinnerID), null,
                    response -> {
                        Meal meal = JsonParser.parseGenericObject(response, Meal.class);
                        if (meal != null) {
                            currentMealID = meal.getId();
                        }

                        // Initialize MQTT client
                        try
                        {
                            AppPreferences appPreferences = new AppPreferences(context);
                            String clientID = appPreferences.getUserID();

                            mosquitto = new MqttHandler(context, clientID);
                            mosquitto.subscribeToTopic(context,"topic_" + clientID);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(request);
        }
    }

    public ArrayList<Plate> filterPlatesByContent(String keyword) {
        ArrayList<Plate> plates = myDatabase.getPlates();

        if (keyword == null || keyword.isEmpty()) {
            return plates;
        }

        ArrayList<Plate> filteredPlates = new ArrayList<>();

        for (Plate plate : plates) {
            if (plate.getTitle().contains(keyword) || plate.getDescription().contains(keyword)) {
                filteredPlates.add(plate);
            }
        }

        return filteredPlates;
    }

    public void requestReviewGetAll(Context context) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<Review> reviews = myDatabase.getReviews();

            if (reviewsListener != null) {
                reviewsListener.onRefreshReviews(reviews);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.Review(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Review> reviews = JsonParser.parseGenericList(response, Review.class);

                    if (reviews.size() == 0) {
                        BetterToast(context, "Sem avaliações!");
                    }

                    myDatabase.setReviews(reviews);

                    if (reviewsListener != null) {
                        reviewsListener.onRefreshReviews(reviews);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };
            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void requestReviewAdd(Context context, int plate_id, int value, String description) {

        if (plate_id == 0) {
            BetterToast(context, "Prato inválido!");
            return;
        }

        if (description == "") {
            BetterToast(context, "Descrição vazia!");
            return;
        }

        if (value == 0) {
            BetterToast(context, "Rating inválido!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<Review> reviews = myDatabase.getReviews();

            if (reviewsListener != null) {
                reviewsListener.onRefreshReviews(reviews);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Route.Review(context),
                    response -> {
                        JSONObject jsonObject =  JsonParser.parseRequest(response);

                        Review review;

                        try {
                            review = new Review(jsonObject);
                        } catch (Exception e) {
                            BetterToast(context, response);
                            throw new RuntimeException(e);
                        }

                        Toast.makeText(context, "Your review was submitted!!", Toast.LENGTH_SHORT).show();
                        myDatabase.addReviewDB(review);

                        requestReviewGetAll(context);
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("plate_id", plate_id + "");
                    params.put("description", description);
                    params.put("value", value + "");
                    return params;
                }
            };

            volleyQueue.add(stringRequest);
        }
    }

    public void requestReviewEdit(Context context, int reviewId, int value, String description) {
        if (description.isEmpty()) {
            BetterToast(context, "Descrição vazia!");
            return;
        }

        if (value == 0) {
            BetterToast(context, "Rating inválido!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<Review> reviews = myDatabase.getReviews();

            if (reviewsListener != null) {
                reviewsListener.onRefreshReviews(reviews);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.PUT,
                    Route.Review(context, reviewId),
                    response -> {
                        JSONObject jsonObject =  JsonParser.parseRequest(response);

                        Review review;

                        try {
                            review = new Review(jsonObject);
                        } catch (Exception e) {
                            BetterToast(context, response);
                            throw new RuntimeException(e);
                        }

                        myDatabase.updateReviewDB(review);
                        Toast.makeText(context, "Your review was updated!", Toast.LENGTH_SHORT).show();

                        requestReviewGetAll(context);
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("description", description);
                    params.put("value", String.valueOf(value)); //Quando se usa o volley os parametros ("chave" e "valor") são ambos tratados como uma string, daí estar a fazer a conversão para string.
                    return params;
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }
    public void requestReviewDelete(final Context context, final int reviewId) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<Review> reviews = myDatabase.getReviews();

            if (reviewsListener != null) {
                reviewsListener.onRefreshReviews(reviews);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.DELETE,
                    Route.Review(context, reviewId), // Assuming Route.Review is correctly implemented to handle review deletion
                    response -> {
                        // Handle successful deletion if needed
                        myDatabase.removeReviewDB(reviewId);
                        Toast.makeText(context, "Avaliação apagada com sucesso!", Toast.LENGTH_SHORT).show();

                        requestReviewGetAll(context);
                    },
                    error -> Route.HandleApiError(context, error)) {

                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    public Review dbGetReview(int id) {
        ArrayList<Review> items = myDatabase.getReviews();

        for (Review item:items) {
            if (item.getId() == id)
                return item;
        }

        return null;
    }

    public void requestTicketGetAll(Context context) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<HelpTicket> helptickets = myDatabase.getTickets();

            if (ticketsListener != null) {
                ticketsListener.onRefreshTickets(helptickets);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.Helpticket(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<HelpTicket> tickets = JsonParser.parseGenericList(response, HelpTicket.class);

                    if (tickets.size() == 0) {
                        BetterToast(context, "Sem tickets!");
                    }

                    myDatabase.SetTickets(tickets);

                    if (ticketsListener != null) {
                        ticketsListener.onRefreshTickets(tickets);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };
            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void requestTicketAdd(Context context, String description) {

        if (description == "") {
            BetterToast(context, "Descrição vazia!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<HelpTicket> tickets = myDatabase.getTickets();

            if (ticketsListener != null) {
                ticketsListener.onRefreshTickets(tickets);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Route.Helpticket(context),
                    response -> {
                        JSONObject jsonObject =  JsonParser.parseRequest(response);

                        HelpTicket ticket;

                        try {
                            ticket = new HelpTicket(jsonObject);
                        } catch (Exception e) {
                            BetterToast(context, response);
                            throw new RuntimeException(e);
                        }

                        Toast.makeText(context, "Your review was submitted!!", Toast.LENGTH_SHORT).show();
                        myDatabase.addTicketDB(ticket);

                        requestTicketGetAll(context);
                    },
                    error -> Route.HandleApiError(context, error)) {
                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("description", description);
                    return params;
                }
            };

            volleyQueue.add(stringRequest);
        }
    }



    public HelpTicket dbGetTicket(int id) {
        ArrayList<HelpTicket> items = myDatabase.getTickets();

        for (HelpTicket item:items) {
            if (item.getId() == id)
                return item;
        }

        return null;
    }

    public void requestTicketDelete(final Context context, final int ticketId) {
        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");

            ArrayList<HelpTicket> tickets = myDatabase.getTickets();

            if (ticketsListener != null) {
                ticketsListener.onRefreshTickets(tickets);
            } else {
                BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
            }
        } else {
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.DELETE,
                    Route.Helpticket(context, ticketId), // Assuming Route.Review is correctly implemented to handle review deletion
                    response -> {
                        // Handle successful deletion if needed
                        myDatabase.removeTicketDB(ticketId);
                        Toast.makeText(context, "Ticket Deleted!", Toast.LENGTH_SHORT).show();

                        requestTicketGetAll(context);
                    },
                    error -> Route.HandleApiError(context, error)) {

                @Override
                public Map<String, String> getHeaders() {
                    return Route.GetAuthorizationBearerHeader(context);
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }
}

