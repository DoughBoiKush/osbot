package com.thatgamerblue.osbot.util;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.API;

import java.util.HashMap;

public class BankUtil extends API {
	
	private static HashMap<String, CArea> banks = new HashMap<String, CArea>();
	private static final String[] names =
			{"HOSIDIUS_COOKING", "RAIDS", "WINTERTODT", "LOVAKENGJ_BLAST_MINE", "LOVAKENGJ_MINE",
					"LOVAKENGJ_SULFUR_MINE",
					"BARB_ASSAULT", "BURGH_DE_ROTT", "CRAFTING_GUILD", "ETCETERIA", "CASTLE_WARS", "VARROCK_EAST",
					"LOVAKENGJ_HOUSE", "FALADOR_EAST", "CAMELOT",
					"CANIFIS", "GRAND_EXCHANGE", "PEST_CONTROL", "EDGEVILLE", "ARDOUGNE_SOUTH", "YANILLE",
					"HOSIDIUS_HOUSE", "GNOME_STRONGHOLD", "CATHERBY",
					"VARROCK_WEST", "PISCARILIUS_HOUSE", "FALADOR_WEST", "TZHAAR", "ARDOUGNE_NORTH", "DRAYNOR",
					"ARCEUUS_HOUSE", "AL_KHARID", "LUMBRIDGE_LOWER",
					"SHAYZIEN_HOUSE", "LUMBRIDGE_UPPER", "DUEL_ARENA", "LUNARS_ISLE"};
	private static final CArea[] areas =
			{new CArea(1653, 3606, 1659, 3612), new CArea(1252, 3570, 1256, 3573), new CArea(1638, 3942, 1641, 3946),
					new CArea(1501, 3851, 1508, 3860), new CArea(1434, 3821, 1440, 3835),
					new CArea(1452, 3859, 1460, 3853),
					new CArea(2534, 3576, 2537, 3572), new CArea(3496, 3213, 3499, 3210),
					new CArea(2933, 3284, 2936, 3281), new CArea(2618, 3896, 2620, 3893),
					new CArea(new int[][]{{2435, 3081}, {2435, 3085}, {2437, 3085}, {2437, 3088}, {2435, 3088},
							{2435, 3092}, {2446, 3092}, {2446, 3087}, {2445, 3086}, {2445, 3085}, {2447, 3083},
							{2447, 3082}, {2446, 3081}, {2443, 3081}, {2442, 3082}, {2439, 3082}, {2438, 3081}}),
					new CArea(new int[][]{{3250, 3416}, {3250, 3424}, {3258, 3424}, {3258, 3416}}),
					new CArea(new int[][]{{1519, 3736}, {1519, 3743}, {1534, 3743}, {1534, 3736}}),
					new CArea(new int[][]{{3009, 3353}, {3009, 3359}, {3019, 3359}, {3019, 3357}, {3022, 3357},
							{3022, 3353}}),
					new CArea(new int[][]{{2724, 3487}, {2724, 3490}, {2721, 3490}, {2721, 3496}, {2731, 3496},
							{2731, 3490}, {2727, 3490}, {2727, 3487}}),
					new CArea(new int[][]{{3509, 3474}, {3509, 3478}, {3508, 3479}, {3508, 3481}, {3509, 3482},
							{3509, 3484}, {3517, 3484}, {3517, 3477}, {3515, 3475}, {3514, 3475}, {3513, 3474}}),
					new CArea(new int[][]{{3159, 3484}, {3159, 3496}, {3171, 3496}, {3171, 3484}}),
					new CArea(new int[][]{{2665, 2651}, {2665, 2656}, {2670, 2656}, {2670, 2651}}),
					new CArea(new int[][]{{3091, 3488}, {3091, 3493}, {3090, 3494}, {3090, 3496}, {3091, 3497},
							{3091, 3500}, {3099, 3500}, {3099, 3488}}),
					new CArea(new int[][]{{2649, 3280}, {2649, 3288}, {2659, 3288}, {2659, 3280}}),
					new CArea(new int[][]{{2609, 3088}, {2609, 3098}, {2617, 3098}, {2617, 3088}}),
					new CArea(new int[][]{{1672, 3559}, {1672, 3576}, {1681, 3576}, {1681, 3559}}),
					(new CArea(new int[][]{{2444, 3415}, {2444, 3418}, {2445, 3418}, {2445, 3422}, {2443, 3422},
							{2443, 3427}, {2445, 3427}, {2445, 3431}, {2444, 3431}, {2444, 3435}, {2448, 3435},
							{2448, 3430}, {2447, 3430}, {2447, 3428}, {2449, 3428}, {2449, 3421}, {2447, 3421},
							{2447, 3419}, {2448, 3419}, {2448, 3415}})).setPlane(1),
					new CArea(new int[][]{{2806, 3438}, {2806, 3446}, {2813, 3446}, {2813, 3438}}),
					new CArea(new int[][]{{3180, 3433}, {3180, 3448}, {3191, 3448}, {3191, 3433}}),
					new CArea(new int[][]{{1795, 3785}, {1795, 3793}, {1811, 3793}, {1811, 3785}}),
					new CArea(new int[][]{{2945, 3366}, {2945, 3368}, {2943, 3368}, {2943, 3374}, {2948, 3374},
							{2948, 3370}, {2950, 3370}, {2950, 3366}}),
					new CArea(new int[][]{{2434, 5179}, {2434, 5190}, {2457, 5190}, {2457, 5174}, {2452, 5174},
							{2450, 5174}, {2448, 5175}, {2447, 5174}, {2444, 5174}, {2442, 5176}, {2439, 5176},
							{2438, 5175}}),
					new CArea(new int[][]{{2612, 3330}, {2612, 3336}, {2615, 3336}, {2615, 3335}, {2619, 3335},
							{2619, 3336}, {2622, 3336}, {2622, 3330}}),
					new CArea(new int[][]{{3088, 3240}, {3088, 3247}, {3098, 3247}, {3098, 3240}}),
					new CArea(new int[][]{{1622, 3737}, {1622, 3753}, {1626, 3753}, {1626, 3750}, {1633, 3750},
							{1633, 3750}, {1633, 3753}, {1638, 3753}, {1638, 3737}}),
					new CArea(new int[][]{{3265, 3161}, {3265, 3174}, {3273, 3174}, {3273, 3161}}),
					new CArea(new int[][]{{3217, 9620}, {3217, 9624}, {3220, 9624}, {3220, 9620}}),
					new CArea(new int[][]{{1496, 3613}, {1496, 3621}, {1514, 3621}, {1514, 3613}}),
					(new CArea(new int[][]{{3207, 3215}, {3207, 3223}, {3210, 3223}, {3211, 3222}, {3211, 3216},
							{3210, 3215}})).setPlane(2),
					new CArea(new int[][]{{3380, 3267}, {3380, 3272}, {3381, 3274}, {3384, 3274}, {3385, 3273},
							{3385, 3267}}), new CArea(2097, 3917, 2104, 3921)};
	
	
	@Override
	public void initializeModule() {
		
		//log("init module");
		//log(names.length);
		//log(areas.length);
		
		for (int i = 0; i < areas.length; i++) {
			banks.put(names[i], areas[i]);
			//log(i);
		}
	}
	
	public String[] getBankNames() {
		return names;
	}
	
	public int bankCount() {
		return banks.size();
	}
	
	public CArea get(String s) {
		return banks.getOrDefault(s, null);
	}
	
	public CArea[] getBanks() {
		return banks.values().toArray(new CArea[0]);
	}
	
	public void printAll() {
		log(areas[22].toString());
	}
}
