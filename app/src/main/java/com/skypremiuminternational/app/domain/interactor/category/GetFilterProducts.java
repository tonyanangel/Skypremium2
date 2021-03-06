package com.skypremiuminternational.app.domain.interactor.category;

import android.util.Log;

import com.skypremiuminternational.app.data.network.request.ProductListRequest;
import com.skypremiuminternational.app.domain.DataManager;
import com.skypremiuminternational.app.domain.executor.PostExecutionThread;
import com.skypremiuminternational.app.domain.executor.ThreadExecutor;
import com.skypremiuminternational.app.domain.interactor.base.UseCase;
import com.skypremiuminternational.app.domain.models.category.FilterListResponse;
import com.skypremiuminternational.app.domain.models.category.FilterResultResponse;
import com.skypremiuminternational.app.domain.models.wellness.ItemsItem;
import com.skypremiuminternational.app.domain.models.wellness.ProductListResponse;
import com.skypremiuminternational.app.domain.util.ProductUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

import static com.skypremiuminternational.app.app.utils.Constants.STATUS_VALID_PRODUCT;

public class GetFilterProducts  extends UseCase<ProductListResponse, String> {
  
  private final ProductUtil productUtil;
  
  @Inject
  protected GetFilterProducts(DataManager dataManager, ThreadExecutor subscriberThread,
                      PostExecutionThread observerThread, ProductUtil productUtil) {
    super(dataManager, subscriberThread, observerThread);
    this.productUtil = productUtil;
  }
  
  @Override
  public Observable<ProductListResponse> provideObservable(final String params) {
    
    return getDataManager().getFilterResult(params)
            .doOnNext(responseBody -> {
                Log.d("JSON-PRODUCT-BEFORE1" ,""+responseBody.getItems());
            })
            .flatMap(response -> Observable.from(response.getItems())
                    .filter(item -> item.getStatus() == STATUS_VALID_PRODUCT)
                    .map(productUtil::flatInfo)
                    .toList()
                    .map((List<ItemsItem> itemsItems) -> {
                      response.setItems(itemsItems);
                      Log.d("JSON-PRODUCT-BEFORE" ,""+response.toString());
                      return response;
                    }));
   
  }
}
