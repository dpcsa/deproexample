package com.example.deproexample;

import com.dpcsa.compon.base.DeclareScreens;
import com.dpcsa.compon.interfaces_classes.Menu;

public class MyDeclareScreens extends DeclareScreens {

    public final static String
            MAIN = "main", DRAWER = "DRAWER", CATALOG = "CATALOG",
            DESCRIPT = "DESCRIPT", CHARACTERISTIC = "CHARACTERISTIC",
            PRODUCT_LIST = "PRODUCT_LIST", PRODUCT_DESCRIPT = "PRODUCT_DESCRIPT",
            FITNESS = "FITNESS";

    @Override
    public void declare() {
        activity(MAIN, R.layout.activity_main)
                .navigator(finishDialog(R.string.attention, R.string.finishOk))
                .drawer(R.id.drawer, R.id.content_frame, R.id.left_drawer, null, DRAWER);

        fragment(DRAWER, R.layout.fragment_drawer)
                .menu(model(menu), view(R.id.recycler));

        fragment(CATALOG, R.layout.fragment_catalog)
                .navigator(handler(R.id.back, VH.OPEN_DRAWER))
                .component(TC.RECYCLER_HORIZONTAL, model(Api.NEWS_PROD).pagination().progress(R.id.progr),
                        view(R.id.recycler_news, R.layout.item_news_prod),
                        navigator(start(PRODUCT_DESCRIPT)))
                .component(TC.RECYCLER, model(Api.CATALOG),
                        view(R.id.recycler, "expandedLevel", new int[]{R.layout.item_catalog_type_1,
                                R.layout.item_catalog_type_2, R.layout.item_catalog_type_3})
                                .expanded(R.id.expand, R.id.expand, model(Api.CATALOG_EX, "catalog_id")),
                        navigator(start(PRODUCT_LIST)));

        activity(PRODUCT_LIST, R.layout.activity_product_list, "%1$s", "catalog_name").animate(AS.RL)
                .navigator(back(R.id.back))
                .component(TC.RECYCLER, model(Api.PRODUCT_LIST, "expandedLevel,catalog_id"),
                        view(R.id.recycler, R.layout.item_product_list)
                                .visibilityManager(visibility(R.id.bonus_i, "bonus"),
                                        visibility(R.id.gift_i, "gift"),
                                        visibility(R.id.newT, "new_product")),
                        navigator(start(PRODUCT_DESCRIPT)));

        activity(PRODUCT_DESCRIPT, R.layout.activity_product_descript, "%1$s", "catalog_name").animate(AS.RL)
                .navigator(back(R.id.back))
                .setValue(item(R.id.product_name, TS.PARAM, "product_name"))
                .component(TC.PAGER_F, view(R.id.pager, DESCRIPT, CHARACTERISTIC)
                        .setTab(R.id.tabs, R.array.descript_tab_name));

        fragment(DESCRIPT, R.layout.fragment_descript)
                .component(TC.PANEL, model(Api.PRODUCT_ID, "product_id"),
                        view(R.id.panel).visibilityManager(visibility(R.id.bonus, "bonus")))
                .component(TC.RECYCLER, model(Api.ANALOG_ID_PRODUCT,"product_id"),
                        view(R.id.recycler, R.layout.item_product_list).noDataView(R.id.not_analog)
                                .visibilityManager(visibility(R.id.bonus_i, "bonus"),
                                        visibility(R.id.gift_i, "gift"),
                                        visibility(R.id.newT, "new_product")),
                        navigator(start(0, PRODUCT_DESCRIPT, PS.RECORD),
                                handler(0, VH.BACK))) ;

        fragment(CHARACTERISTIC, R.layout.fragment_characteristic)
                .component(TC.RECYCLER, model(Api.CHARACT_ID_PRODUCT, "product_id"),
                        view(R.id.recycler, "2", new int[] {R.layout.item_property, R.layout.item_property_1}));

        fragment(FITNESS, R.layout.fragment_fitness)
                .navigator(handler(R.id.back, VH.OPEN_DRAWER))
                .component(TC.SPINNER, model(JSON, getString(R.string.clubs)),
                        view(R.id.spinner, R.layout.item_spin_drop, R.layout.item_spin_hider))
                .component(TC.RECYCLER, model(Api.FITNESS, "clubId"),
                        view(R.id.recycler, R.layout.item_fitness), null).eventFrom(R.id.spinner);
    }

    Menu menu = new Menu()
            .item(R.drawable.list, R.string.m_catalog, CATALOG, true)
            .divider()
            .item(R.drawable.ic_aura, R.string.fitness, FITNESS);
}