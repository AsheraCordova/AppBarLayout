// start - imports

export const enum Layout_scrollFlags {
noScroll = "noScroll",
scroll = "scroll",
exitUntilCollapsed = "exitUntilCollapsed",
enterAlways = "enterAlways",
enterAlwaysCollapsed = "enterAlwaysCollapsed",
snap = "snap",
snapMargins = "snapMargins",
}	
import CommandAttr from '../../widget/CommandAttr';
import IWidget from '../../widget/IWidget';
import ILayoutParam from '../../widget/ILayoutParam';
import {plainToClass, Type, Exclude, Expose, Transform} from "class-transformer";
import 'babel-polyfill';
import {Gravity} from '../../widget/TypeConstants';
import {ITranform, TransformerFactory} from '../../widget/TransformerFactory';
import {Event} from '../../app/Event';
import {MotionEvent} from '../../app/MotionEvent';
import {DragEvent} from '../../app/DragEvent';
import {KeyEvent} from '../../app/KeyEvent';
import { ScopedObject } from '../../app/ScopedObject';



export class Layout_scrollFlagsTransformer implements ITranform {
    transform(value: any, obj: any, type: number) : any{
        if (type == 1) {
            return value.toString().replace(",", "|");
        } else {
            let strArray:Array<string> = value.toString().split("|");
            
            let valueArr:Array<Layout_scrollFlags> = new Array<Layout_scrollFlags>();
            for (let i =0; i < strArray.length; i++) {
                switch(strArray[i]) {
					case "noScroll":
						valueArr.push(Layout_scrollFlags.noScroll);
                       	break;	
					case "scroll":
						valueArr.push(Layout_scrollFlags.scroll);
                       	break;	
					case "exitUntilCollapsed":
						valueArr.push(Layout_scrollFlags.exitUntilCollapsed);
                       	break;	
					case "enterAlways":
						valueArr.push(Layout_scrollFlags.enterAlways);
                       	break;	
					case "enterAlwaysCollapsed":
						valueArr.push(Layout_scrollFlags.enterAlwaysCollapsed);
                       	break;	
					case "snap":
						valueArr.push(Layout_scrollFlags.snap);
                       	break;	
					case "snapMargins":
						valueArr.push(Layout_scrollFlags.snapMargins);
                       	break;	
                }
                
            }
            return valueArr;
        }
    }
}

import {ViewGroupImpl_LayoutParams} from './ViewGroupImpl';

// end - imports
import {ViewGroupImpl} from './ViewGroupImpl';
export abstract class AppBarLayoutImpl<T> extends ViewGroupImpl<T>{
	//start - body
	static initialize() {
		TransformerFactory.getInstance().register("layout_scrollFlags", new Layout_scrollFlagsTransformer());
    }	
	@Type(() => CommandAttr)
	@Expose({ name: "expanded" })
	expanded!:CommandAttr<boolean>| undefined;

	@Exclude()
	protected thisPointer: T;	
	protected abstract getThisPointer(): T;
	reset() : T {	
		super.reset();
		this.expanded = undefined;
		return this.thisPointer;
	}
	constructor(id: string, path: string[], event:  string) {
		super(id, path, event);
		this.thisPointer = this.getThisPointer();
	}
	

	public setExpanded(value : boolean) : T {
		this.resetIfRequired();
		if (this.expanded == null || this.expanded == undefined) {
			this.expanded = new CommandAttr<boolean>();
		}
		
		this.expanded.setSetter(true);
		this.expanded.setValue(value);
		this.orderSet++;
		this.expanded.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		
	//end - body

}
	
//start - staticinit
export abstract class AppBarLayoutImpl_LayoutParams<T> extends ViewGroupImpl_LayoutParams<T> {
	@Type(() => CommandAttr)
	@Expose({ name: "layout_scrollFlags" })
	layout_scrollFlags!:CommandAttr<Layout_scrollFlags[]>| undefined;
	@Type(() => CommandAttr)
	@Expose({ name: "layout_scrollInterpolator" })
	layout_scrollInterpolator!:CommandAttr<string>| undefined;
	@Exclude()
	protected thisPointer: T;	
	protected abstract getThisPointer(): T;
	reset() : T {	
		super.reset();
		this.layout_scrollFlags = undefined;
		this.layout_scrollInterpolator = undefined;
		return this.thisPointer;
	}
	constructor() {
		super();
		this.thisPointer = this.getThisPointer();
	}
	
	public setLayoutScrollFlags(...value : Layout_scrollFlags[]) : T {
		if (this.layout_scrollFlags == null || this.layout_scrollFlags == undefined) {
			this.layout_scrollFlags = new CommandAttr<Layout_scrollFlags[]>();
		}
		this.layout_scrollFlags.setSetter(true);
		this.layout_scrollFlags.setValue(value);
		this.orderSet++;
		this.layout_scrollFlags.setOrderSet(this.orderSet);
this.layout_scrollFlags.setTransformer('layout_scrollFlags');		return this.thisPointer;
	}
	public setLayoutScrollInterpolator(value : string) : T {
		if (this.layout_scrollInterpolator == null || this.layout_scrollInterpolator == undefined) {
			this.layout_scrollInterpolator = new CommandAttr<string>();
		}
		this.layout_scrollInterpolator.setSetter(true);
		this.layout_scrollInterpolator.setValue(value);
		this.orderSet++;
		this.layout_scrollInterpolator.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
}

export class AppBarLayout_LayoutParams extends AppBarLayoutImpl_LayoutParams<AppBarLayout_LayoutParams> implements ILayoutParam {
    getThisPointer(): AppBarLayout_LayoutParams {
        return this;
    }

   	constructor() {
		super();	
	}
}

export class AppBarLayout extends AppBarLayoutImpl<AppBarLayout> implements IWidget{
    getThisPointer(): AppBarLayout {
        return this;
    }
    
   	public getClass() {
		return AppBarLayout;
	}
	
   	constructor(id: string, path: string[], event: string) {
		super(id, path, event);	
	}
}

AppBarLayoutImpl.initialize();

//end - staticinit
