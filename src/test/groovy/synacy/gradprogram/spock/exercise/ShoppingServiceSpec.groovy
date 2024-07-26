package synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.exercise.Cart
import com.synacy.gradprogram.spock.exercise.Courier
import com.synacy.gradprogram.spock.exercise.DeliveryRequest
import com.synacy.gradprogram.spock.exercise.DeliveryRequestRepository
import com.synacy.gradprogram.spock.exercise.DeliveryService
import com.synacy.gradprogram.spock.exercise.Item
import com.synacy.gradprogram.spock.exercise.ItemType
import com.synacy.gradprogram.spock.exercise.Order
import com.synacy.gradprogram.spock.exercise.OrderRepository
import com.synacy.gradprogram.spock.exercise.OrderStatus
import com.synacy.gradprogram.spock.exercise.OrderSummary
import com.synacy.gradprogram.spock.exercise.OrderingService
import com.synacy.gradprogram.spock.exercise.ShoppingService
import com.synacy.gradprogram.spock.exercise.User
import spock.lang.Specification

class ShoppingServiceSpec extends Specification {

    ShoppingService shoppingService

    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        shoppingService = new ShoppingService(orderingService, deliveryService, orderRepository, deliveryRequestRepository)
    }

    def "buyNonSpoilingItemsInCart will apply discount to cart items and create an order and delivery"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(), [
                new Item("Television", 750.0, ItemType.APPLIANCE),
                new Item("Eggs", 200.0, ItemType.FOOD)
        ])
        def user = new User()

        user.setFirstName("John")
        user.setLastName("Denver")
        user.setAddress("West Virginia")

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(cart)
        1 * orderingService.createAnOrder(cart, "John Denver", "West Virginia", false) >> new Order()
        1 * deliveryService.createDelivery(_)
    }

    def "getOrderSummary should fetch order by id and delivery request by order id and return order summary"() {
        given:
        def order = new Order()
        UUID orderUUID = order.id
        order.setTotalCost(1000.0)
        order.setStatus(OrderStatus.PENDING)

        def deliveryRequest = new DeliveryRequest()
        deliveryRequest.setDeliveryDate(new Date())
        deliveryRequest.setCourier(Courier.GRAB)

        when:
        OrderSummary orderSummary = shoppingService.getOrderSummary(orderUUID)

        then:
        1 * orderRepository.fetchOrderById(orderUUID) >> order
        1 * deliveryRequestRepository.fetchDeliveryRequestByOrderId(orderUUID) >> deliveryRequest

        orderSummary.getTotalCost() == 1000.0
        orderSummary.getStatus() == OrderStatus.PENDING
        orderSummary.getDeliveryDate() == deliveryRequest.getDeliveryDate()
        orderSummary.getCourier() == Courier.GRAB
    }
}