package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter { driver -> trips.none { it.driver == driver } }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.partition { item ->
            trips.sumBy { trip ->
                if (item in trip.passengers) 1 else 0
            } >= minTrips
        }.first.toSet()


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        allPassengers.filter { item ->
            trips.count { it.driver == driver && item in it.passengers } > 1
        }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        allPassengers.associate { item ->
            item to trips.filter { trip -> item in trip.passengers }
        }.filterValues { g ->
            val (discount, noDiscount) = g.partition { it.discount != null }
            discount.size > noDiscount.size
        }.keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return trips.groupBy {
        val startPeriod = it.duration / 10 * 10
        val endPeriod = startPeriod + 9
        startPeriod..endPeriod
    }.toList().maxBy { (_, g) -> g.size }?.first
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val income = trips.sumByDouble { trip -> trip.cost }
    val sortedIncome: List<Double> = trips.groupBy { trip -> trip.driver }.map { (_, drivers) ->
        drivers.sumByDouble { trip -> trip.cost }
    }.sortedDescending()
    val topDriversNumber = (0.2 * allDrivers.size).toInt()
    val incomingDriviers = sortedIncome.take(topDriversNumber).sum()
    return incomingDriviers >= 0.8 * income
}