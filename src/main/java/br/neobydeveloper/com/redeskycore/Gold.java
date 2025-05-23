package br.neobydeveloper.com.redeskycore;

public class Gold {
    private int gold;

    public Gold() {
        this.gold = 0;
    }

    public Gold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void removeGold(int amount) {
        this.gold -= amount;
    }
}
