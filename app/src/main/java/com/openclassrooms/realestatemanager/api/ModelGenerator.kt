package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.*
import java.util.*
import kotlin.collections.ArrayList

class ModelGenerator {
    private var agentGenerator : List<RealEstateAgent> =  ArrayList(setOf(RealEstateAgent("Bernole", "Yohan")))

    private var propertiesGenerator: List<Property> = ArrayList(
        setOf(
            Property(1, Type.APARTMENT, 1_440_000, 76.37f, 3, 1,1, "Enter this stately one bedroom and be " +
                    "immediately greeted by beautiful park and city views from this sun-lit high-floor unit. The pass-thru kitchen has been recently outfitted " +
                    "with all new stainless steel appliances and also features black granite countertops and a breakfast bar. Some other wonderful features of " +
                    "this home include a washer/dryer, generous closet space, a marble bath with built-in medicine cabinets, parquet floors, and new carpeting " +
                    "in the bedroom. This full-service building has an incredible staff, a live-in resident manager, and an unbeatable location-one block from " +
                    "Central Park. The expansive Equinox gym is located directly across the street.", ArrayList(setOf("https://res.listglobally.com/listings/5714386/77227640/b302a56a439e28af6f4c3e7f56ed5a68?mode=max&w=1024",
                "https://res.listglobally.com/listings/5714386/77227640/fb77ecc3cf2fa4361fd5f07a984b662c?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77227640/a3355fc806efd5ca4bc356bdcdb3fa87?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/77227640/9d25d297ecf9cd2472889e86b71d124b?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77227640/a2d072afaf9340d086f83ba6251e8a61?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/77227640/141bf855c3f8c8d26aa240132e342e63?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77227640/9fcfd1e43401972c93bf4e7510d567a0?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/77227640/3d19759b9530a56e0bf05c648f519f67?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77227640/19b2fe05188b45fd7c93a7a4502969e2?mode=max&w=1920")),
                "150 Columbus Avenue Upper West Side, New York", ArrayList(setOf(Option.WASHING_MACHINE, Option.AIR_CONDITIONER)), Status.ON_SALE, Date(1_220_227_200L * 1000), null,
                agentGenerator[0].firstName, null, null),

            Property(2, Type.APARTMENT, 11_000_000, 507.44f, 8, 4, 3, "Welcome to 28 Laight Street," +
                    " #6AB, an incredible Tribeca Trophy with private deeded parking. This dramatic and inspiring pre-war corner loft boasts a sprawling " +
                    "5,462sf layout and 10’2” ceilings - truly unlike anything else on the market. Huge casement windows, exposed beams, and brick throughout" +
                    " create the quintessential ambience of Tribeca elegance.", ArrayList(setOf("https://res.listglobally.com/listings/5714386/77175824/7e490cf70b9689e414afbd0507d7376d?mode=max&w=1920",
                "https://res.listglobally.com/listings/5714386/77175824/2e7601f3ee7f332a998c487a1412e1d9?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77175824/7b4b5064ee4be569e7e9147b0de1906c?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/77175824/6ed3a08c8e64fdf73e8cc22fabe76e47?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77175824/2f21764eaea96aabe0756de04565484a?mode=max&w=1920",
                "https://res.listglobally.com/listings/5714386/77175824/a17ad3bcf0c5a04d5515593c6efeccb2?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77175824/c3aeef38f1c14b85d7a1e64d022917be?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/77175824/d823e2f7336d1fb53d827a0f52bcd1a7?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77175824/b414451893a6e86801ee61edb6514285?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/77175824/bf5a2807405efbb668a6be8abd18b1e4?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/77175824/4752b7c9a0a48f86a326cce2f30ddc7d?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/77175824/578772ef54fd88eed024baecaacba1a9?mode=max&w=1920")),
                "28 Laight Street, New York", ArrayList(setOf(Option.WASHING_MACHINE)), Status.ON_SALE, Date(1_220_227_200L * 1000), null,
                agentGenerator[0].firstName, null, null),

            Property(3, Type.HOUSE, 1_850_000, null, 8, 5,3, "JAMAICA ESTATES CHARM! WELCOME TO YOUR NEW HOME. SITUATED ON A 50 X 120 LOT IN THE HEART OF JAMAICA ESTATES TREE LINED STREETS." +
                    " THIS EXPANDED CAPE TUDOR HOME OFFERS 5 BEDROOMS 3 FULL BATH WITH HEATED FLOORS, SPLIT SYSTEM A/C, OPEN EAT IN KICHEN DESIGN," +
                    " HARDWOOD FLOORS & MUCH MORE. LOCATED CLOSE TO ALL SHOPS, TRANSPORTATION & HOUSE OF WORSHIPS.", ArrayList(setOf("https://res.listglobally.com/listings/5681536/76682536/24b415d0879781a28f615483717e468c?mode=max&w=1920",
                "https://res.listglobally.com/listings/5681536/76682536/e6d1457aa1f5c03655ca377363452270?mode=max&w=1920", "https://res.listglobally.com/listings/5681536/76682536/cf8915879aa3643dcc8a47a84906d238?mode=max&w=1920",
                "https://res.listglobally.com/listings/5681536/76682536/c9ec3825faea9eb9d1b079d306a24341?mode=max&w=1920", "https://res.listglobally.com/listings/5681536/76682536/25cc8f8d61bb71d2840f362018d36e0f?mode=max&w=1920",
            "https://res.listglobally.com/listings/5681536/76682536/ae92522dae5980d248270b3b0d70291e?mode=max&w=1920", "https://res.listglobally.com/listings/5681536/76682536/83b4c3528cebf5a28d6b04d74bec1fb2?mode=max&w=1920")),
                    "181-69 TUDOR RD, New York", ArrayList(setOf(Option.DISHWASHER, Option.WASHING_MACHINE)), Status.ON_SALE, Date(1_220_227_200L * 1000), null,
                    agentGenerator[0].firstName, null, null),

            Property(5, Type.APARTMENT, 3_750_000, 376.26f, 11,4,3, "Incredible location near transportation, Whole" +
                    " Foods, Trader Joe's, Chelsea Market, fantastic restaurants, close to the Highline, Penn Station, Hudson Yards, and shops. Well maintained" +
                    " Cooperative. Parking nearby! Low Maintenance \$3,900/mo. and a \$1,600/ mo. assessment).", ArrayList(setOf("https://res.listglobally.com/listings/5714386/75642116/812be33df63e2f47622a280477db0322?mode=max&w=1920",
                "https://res.listglobally.com/listings/5714386/75642116/5acc439cb3e075c60125c5fb13b1015c?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/75642116/646e9aef09585fa93c3ae5035250a799?mode=max&w=1920",
                "https://res.listglobally.com/listings/5714386/75642116/7c8875e26a0f609894eea90306f41a99?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/75642116/78e39d7e9cb0af62728d04db089476b0?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/75642116/6466191e8ae0d61bab387542fb4e83b4?mode=max&w=1920")),
                "143 West 20th Street, New York", null, Status.ON_SALE, Date(1_220_227_200L * 1000), null,
                agentGenerator[0].firstName, null, null),

            Property(6, Type.APARTMENT, 899_000, 58.5f, 3, 1,1,"Your oversized, loft-like home awaits in this inviting" +
                    " pre-war one-bedroom that can be easily converted to a one-bedroom plus den or home office.", ArrayList(setOf("https://res.listglobally.com/listings/5714386/75557942/e87856f193b0eb01baa18f7d59144e20?mode=max&w=1920",
                "https://res.listglobally.com/listings/5714386/75557942/ceec61a53432327caf6e189a00dee528?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/75557942/9d41b6b9ef6bc94c370a83206a5d8861?mode=max&w=1920",
            "https://res.listglobally.com/listings/5714386/75557942/490d50e9c8c42805b9b7f13a4bfc331f?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/75557942/418308834cc1e632e4129c78c3885186?mode=max&w=1920")),
                "110 West 94th Street, New York", null, Status.ON_SALE, Date(1_220_227_200L * 1000), null,
                agentGenerator[0].firstName, null, null),

            Property(7, Type.APARTMENT, 599_000, 65.59f, 3, 1,1,"FOR INVESTORS ONLY: SOLD WITH TENANT IN-PLACE. This South-facing one bedroom condo in Yorkville is the perfect home base" +
                    " or pied-a-terre in the desirable Upper East Side neighborhood of Manhattan. The open plan kitchen-living-dining area features a fully equipped, modern kitchen with dishwasher.",
                ArrayList(setOf("https://res.listglobally.com/listings/5706262/75421176/f4d606f40f7b6cf6186b0e5a93d3aa3b?mode=max&w=1920","https://res.listglobally.com/listings/5706262/75421176/89020c0772c5e8174ad8a3759a8a66f7?mode=max&w=1920",
                "https://res.listglobally.com/listings/5706262/75421176/9ef5a905a584d7f65b15589e6ef8569b?mode=max&w=1920", "https://res.listglobally.com/listings/5706262/75421176/3d036f567ebb93c21f3f7c505e0ab9cc?mode=max&w=1920")),
                "206 East 95th Street, #5A, New York", ArrayList(setOf(Option.ELEVATOR, Option.DISHWASHER)), Status.ON_SALE, Date(1_220_227_200L * 1000), null,
                agentGenerator[0].firstName, null, null),

            Property(8, Type.APARTMENT, 499_000, 111.48f, 6,2,2, "Welcome to 1840 East 13th. Looking for a perfectly laid out true two bedroom co-op? Here it is, welcome to apartment 5F at 1840 East 13th Street." +
                    " Conveniently located steps away from Kings highway, you would be hard pressed to find a better more affordable apartment.",
                ArrayList(setOf("https://res.listglobally.com/listings/5714386/75171703/24bef41fd4492c6cf1aad4bda439e7fe?mode=max&w=1920","https://res.listglobally.com/listings/5714386/75171703/c728dbd7e6aa60ece6ecf76de424b98f?mode=max&w=1920",
                "https://res.listglobally.com/listings/5714386/75171703/24d1a60b450d9ff35097f58907646c98?mode=max&w=1920", "https://res.listglobally.com/listings/5714386/75171703/94f2fae98b2584a6ff92577272caba52?mode=max&w=1920")),
                "1840 East 13th Street, New York", null, Status.ON_SALE, Date(1_220_227_200L * 1000), null,
                agentGenerator[0].firstName, null, null),
        ),
    )


    fun getAllProperties() : List<Property>{
        return propertiesGenerator
    }
}