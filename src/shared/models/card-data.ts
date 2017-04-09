export class CardData {
	public id: string;
    public parentId: string;
    public cardIds: Array<string>;
    public lastUpdate: string;
    public status: number;

    constructor () {
    	this.id = "";
    	this.parentId = "";
    	this.cardIds = [];
    	this.lastUpdate = "";
    	this.status = 0;
    }
}