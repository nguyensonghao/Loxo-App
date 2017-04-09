export class GameObject {
    public id: number;
    public gameType: string;
    public endGame: boolean;   

    constructor (id: number, gameType: string) {
        this.id = id;
        this.gameType = gameType;
        this.endGame = false;
    } 
}