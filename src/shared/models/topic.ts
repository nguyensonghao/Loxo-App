export class Topic {	
	public id: string;
	public userId: number;
	public userName: string;
	public parentId: string;
	public lastUpdate: string;
	public level: number ;
	public status: number;
	public appId: number;
	public price: number;
	public priority: number;
	public name: string;
	public description: string;
	public avatar: string;
	public childrentIds: string;
	public totalCardNum: number;
	public languageId: number;

	constructor () {
		this.id = "";
		this.userId = 0;
		this.userName = "";
		this.parentId = "";
		this.lastUpdate = "";
		this.level = 0;
		this.status = 0;
		this.appId = 0;
		this.price = 0;
		this.priority = 0;
		this.name = "";
		this.description = "";
		this.avatar = "";
		this.childrentIds = "";
		this.totalCardNum = 0;
		this.languageId = 0;
	}
}