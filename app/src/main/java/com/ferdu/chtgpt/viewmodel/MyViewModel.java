package com.ferdu.chtgpt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ferdu.chtgpt.database.ChatDatabase;
import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.ResponseModel2;
import com.ferdu.chtgpt.models.TransModel;
import com.ferdu.chtgpt.models.data.ChatModel;
import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.models.data.Example2;
import com.ferdu.chtgpt.models.data.TreadAndChats;
import com.ferdu.chtgpt.models.data.TypeAndExample;
import com.ferdu.chtgpt.models.data.Types;
import com.ferdu.chtgpt.models.data.User;
import com.ferdu.chtgpt.repo.ChatRepository;

import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyViewModel extends AndroidViewModel {
    private final ChatRepository repositories;
    private final ChatDatabase database;

    public MyViewModel(@NonNull Application application) {
        super(application);
        database = ChatDatabase.getDatabase(application);
        repositories = new ChatRepository(database);
    }

    public LiveData<ResponseModel2> getTex(String token, ReqModel reqModel) {
        return repositories.getTex(token, reqModel);
    }

    public void updateUser(User user) {
        if (LCUser.currentUser() != null) {
            LCUser lcUser = LCUser.currentUser();
            lcUser.put("money",user.getMoney());
            lcUser.put("tokens",user.getTokens());
            lcUser.saveInBackground(LCUser.getCurrentUser()).subscribe(new Observer<LCObject>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(LCObject lcObject) {

                }
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }

        repositories.updateData(database.userDao(), user);
    }

    public LiveData<User> getUser() {
        return database.userDao().getUser();
    }

    public void deleteUser() {
        database.userDao().deleteUser();
    }

    public void insertUser(User user) {
        database.userDao().insertD(user);
    }

    public LiveData<TransModel> getTrans(String msg, int type) {
        return repositories.getTrans("https://v.api.aa1.cn/api/api-fanyi-yd/index.php", msg, type);
    }

    public LiveData<List<ChatModel>> getChatModels(int id) {
        return database.chatModelDao().getById(id);
    }

    public int deleteByTreadId(int id) {
        return database.chatModelDao().deleteByTreadId(id);
    }

    public LiveData<List<ChatModel>> getChatModelsAll() {
        return database.chatModelDao().getAll();
    }

    public LiveData<List<ChatThread>> getChatThreadsAll() {
        return database.chatThreadDao().getAll();
    }

    public LiveData<TreadAndChats> getThreadAndChats() {
        return database.chatThreadDao().loadChatsAndTread();
    }

    public LiveData<List<ChatThread>> getCTQuery(String query) {
        return database.chatThreadDao().getQuery("%" + query + "%");
    }

    public LiveData<List<ChatModel>> getCMQuery(String query) {
        return database.chatModelDao().getQuery("%" + query + "%");
    }

    public LiveData<Example2> getExample(int id) {
        return repositories.getExOnId(id);
    }

    public LiveData<List<Example2>> getExamples() {
        return database.exampleDao().getAll();
    }

    public LiveData<List<Example2>> getExOnTypes(String type) {
        return database.exampleDao().getTypes(type);
    }

    public LiveData<List<Example2>> getExOnQuery(String type) {
        return database.exampleDao().getExSearch("%" + type + "%");
    }

    public LiveData<List<Types>> getTypes() {
        return database.typeDao().getTypes();
    }

    public LiveData<List<Types>> getTypesOnEx(int id) {
        return database.typeDao().getTypesFromEx(id);
    }


    public void insertChatModel(ChatModel... chatModel) {
        repositories.insertData(database.chatModelDao(), chatModel);
    }

    public void insertType(Types... type) {
        repositories.insertData(database.typeDao(), type);
    }

    public void insertTypeAndEx(TypeAndExample... type) {
        repositories.insertData(database.typeAndExampleDao(), type);
    }

    public void insertExample(Example2... example) {
        repositories.insertData(database.exampleDao(), example);
    }

    public void insertChatThread(ChatThread... chatThread) {
        repositories.insertData(database.chatThreadDao(), chatThread);
    }

    public LiveData<ChatModel> isExist(String meText, String AIText) {
        return database.chatModelDao().isExist(meText, AIText);
    }

    public LiveData<Integer> selectChatThreadInsertId() {
        return database.chatThreadDao().getId();
    }

    public void updateChatModel(ChatModel... chatModel) {
        repositories.updateData(database.chatModelDao(), chatModel);
    }

    public void updateType(Types... type) {
        repositories.updateData(database.typeDao(), type);
    }

    public void updateExample(Example2... example) {
        repositories.updateData(database.exampleDao(), example);
    }


    public void updateChatThread(ChatThread... chatThread) {
        repositories.updateData(database.chatThreadDao(), chatThread);
    }


    public void deleteChatModel(ChatModel... chatModel) {
        repositories.deleteData(database.chatModelDao(), chatModel);
    }

    public void deleteType(Types... type) {
        repositories.deleteData(database.typeDao(), type);
    }

    public void deleteExample(Example2... example) {
        repositories.deleteData(database.exampleDao(), example);
    }

    public void deleteChatThread(ChatThread... chatThread) {
        repositories.deleteData(database.chatThreadDao(), chatThread);
    }



}
