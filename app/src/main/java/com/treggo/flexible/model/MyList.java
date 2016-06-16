package com.treggo.flexible.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public class MyList extends RealmObject {

    private String name;

    private Card card;

    private RealmList<Card> cards;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public RealmList<Card> getCards() {
        return cards;
    }

    public void setCards(RealmList<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
