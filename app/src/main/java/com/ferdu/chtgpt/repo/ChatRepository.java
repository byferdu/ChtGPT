package com.ferdu.chtgpt.repo;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.ferdu.chtgpt.database.ChatDatabase;
import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.RequestModel;
import com.ferdu.chtgpt.models.ResModel;
import com.ferdu.chtgpt.models.ResponseModel;
import com.ferdu.chtgpt.models.TransModel;
import com.ferdu.chtgpt.models.dao.BaseDao;
import com.ferdu.chtgpt.models.data.Example2;
import com.ferdu.chtgpt.network.HttpClient;
import com.ferdu.chtgpt.network.Requests;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {
    private static final String TAG = "DownStream";
    private final Requests requests;
    private final ChatDatabase database;
    public Call<ResponseModel> myCall;
    public Call<ResModel> resModelCall;

    public ChatRepository(ChatDatabase database) {
        this.database = database;
        requests = HttpClient.getRetrofit().create(Requests.class);

    }

    public LiveData<ResponseModel> getTex(String token, ReqModel reqModel) {
        MutableLiveData<ResponseModel> liveData = new MutableLiveData<>();
        myCall = requests.getTex(token, reqModel);
        myCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.errorBody() == null) {
                    liveData.setValue(response.body());
                }else {
                    Gson gson = new Gson();
                    try {
                        String errorJson = response.errorBody().string();
                        Log.d("ITAG", "onResponse: "+ errorJson);
                        ResponseModel.ErrorParent errorBean = gson.fromJson(errorJson, ResponseModel.ErrorParent.class);
                        ResponseModel responseModel = new ResponseModel(null);
                        responseModel.setErrorParent(errorBean);
                        liveData.setValue(responseModel);
                    } catch (IOException e) {
                        e.printStackTrace();
                        liveData.setValue(new ResponseModel(e.getMessage()!=null&&!e.getMessage().trim().isEmpty()?e.getMessage():call.toString()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                liveData.setValue(new ResponseModel(t.getMessage()!=null&&!t.getMessage().trim().isEmpty()?t.getMessage():call.toString()));
            }
        });
        return liveData;
    }
  public LiveData<ResModel> chatCompletions(String token, RequestModel reqModel) {
        MutableLiveData<ResModel> liveData = new MutableLiveData<>();
        resModelCall = requests.chatCompletions(token, reqModel);
      resModelCall.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(@NonNull Call<ResModel> call, @NonNull Response<ResModel> response) {
                if (response.errorBody() == null) {
                    liveData.setValue(response.body());
                }else {
                    Gson gson = new Gson();
                    try {
                        String errorJson = response.errorBody().string();
                        Log.d("ITAG", "onResponse: "+ errorJson);
                        ResponseModel.ErrorParent errorBean = gson.fromJson(errorJson, ResponseModel.ErrorParent.class);
                        ResModel responseModel2 = new ResModel(null);
                        responseModel2.setErrorParent(errorBean);
                        liveData.setValue(responseModel2);
                    } catch (IOException e) {
                        e.printStackTrace();
                        liveData.setValue(new ResModel(e.getMessage()!=null&&!e.getMessage().trim().isEmpty()?e.getMessage():call.toString()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResModel> call, @NonNull Throwable t) {
                liveData.setValue(new ResModel(t.getMessage()!=null&&!t.getMessage().trim().isEmpty()?t.getMessage():call.toString()));
            }
        });
        return liveData;
    }


    public LiveData<TransModel> getTrans(String url, String msg, int type) {
        MutableLiveData<TransModel> liveData = new MediatorLiveData<>();
        requests.getTransText(url, msg, type).enqueue(new Callback<TransModel>() {
            @Override
            public void onResponse(@NonNull Call<TransModel> call, @NonNull Response<TransModel> response) {
                liveData.setValue(response.body());
                try {
                    Log.d("ProgressTag", "onFa: " + (response.errorBody() != null ? response.errorBody().string() : Objects.requireNonNull(response.body()).toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransModel> call, @NonNull Throwable t) {
                liveData.setValue(null);
                Log.d("ProgressTag", "onFailure: ");
                call.cancel();
                Log.d("ProgressTag", "onFailure: " + call.timeout() +" :\n\n\n "+ t.getLocalizedMessage());
            }
        });
        return liveData;
    }

    public LiveData<Example2> getExOnId(int id) {
        return database.exampleDao().getById(id);
    }


    @SafeVarargs
    public final <I extends BaseDao<T>, T> void insertData(I i, T... t) {
        //  new WordRepository.InsertAsyncTask(wordDao).execute(words);
        new ChatRepository.InsertAsyncTask<>(i).execute(t);
    }

    @SafeVarargs
    public final <I extends BaseDao<T>, T> void updateData(I i, T... t) {
        //  new WordRepository.InsertAsyncTask(wordDao).execute(words);
        new ChatRepository.UpdateAsyncTask<>(i).execute(t);
    }

    @SafeVarargs
    public final <I extends BaseDao<T>, T> void deleteData(I i, T... t) {
        //  new WordRepository.InsertAsyncTask(wordDao).execute(words);
        new ChatRepository.DeleteAsyncTask<>(i).execute(t);
    }

    private static class InsertAsyncTask<I extends BaseDao<T>, T> extends AsyncTask<T, Void, Void> {
        private final I dao;

        private InsertAsyncTask(I dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(T... ts) {
            dao.insertD(ts);
            return null;
        }
    }

    private static class UpdateAsyncTask<I extends BaseDao<T>, T> extends AsyncTask<T, Void, Void> {
        private final I dao;

        private UpdateAsyncTask(I dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(T... ts) {
            dao.updateD(ts);
            return null;
        }
    }

    private static class DeleteAsyncTask<I extends BaseDao<T>, T> extends AsyncTask<T, Void, Void> {
        private final I dao;

        private DeleteAsyncTask(I dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(T... ts) {
            dao.deleteD(ts);
            return null;
        }
    }

}
