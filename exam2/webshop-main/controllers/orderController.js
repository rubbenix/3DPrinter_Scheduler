const orderData = require("../data/order");
const HttpError = require("../errors/HttpError");
const keysCheck = require("../helpers/keys");

let currentAvailableId = 3;
const allowedKeys = ["customer", "print", "colors", "material", "shipping"];
const statusOptions = ["new", "processing", "ready for pick up", "ready for shipping", "completed", "cancelled"]

module.exports = {
    findById: function(id) {
        const found = orderData.find(order => order.id === parseInt(id));
        if(found === undefined) {
            throw new HttpError("Order with id: " + id + " cannot be found", 404);
        }

        return found;
        },
    getAllOrders: function() {
        return orderData;
    },
    patchOrder: function(id, updatedOrder) {
        let oldOrder;
        try {
            oldOrder = this.findById(id);
        } catch(err) {
            throw new HttpError(err.message, err.code);
        }

        for(let key in updatedOrder) {
            if(!oldOrder.hasOwnProperty(key)) {
                throw new HttpError("Cannot patch key " + key + " because the old object does not contain it", 400);
            }
        }

        // Enforce statechange so that an order can only change from a specific state to another.
        if("status" in updatedOrder) {
            if(!statusCheck(oldOrder["status"], updatedOrder["status"])) {
                throw new HttpError("Status" + oldOrder["status"] + " cannot be changed to status " + updatedOrder["status"], 400)
            }
        }

        // Orders can only be updated if they are new. Once they are processing they can no longer be changed.
        if(oldOrder["status"] !== "new" && !(Object.keys(updatedOrder).length === 1 && Object.keys(updatedOrder)[0] === "status")){
            throw new HttpError("Only orders with status New can be changed.", 400)
        }

        Object.assign(oldOrder, updatedOrder);
        return(oldOrder);

    },
    postOrder: function(newOrder) {
        try {
            keysCheck.checkKeys(newOrder, allowedKeys);
        } catch(err) {
            throw new HttpError(err.message, err.code);
        }
        newOrder["id"] = currentAvailableId;
        newOrder["status"] = "new";

        orderData.push(newOrder);
        currentAvailableId++;

        return newOrder;
    },
}

function statusCheck(oldStatus, newStatus) {
    if(!statusOptions.includes(newStatus))
        return false;
    // only new prints can be cancelled
    if(newStatus === "cancelled" && oldStatus !== "new")
        return false;
    if(newStatus === "processing" && oldStatus !== "new")
        return false;
    if((newStatus === "ready for pick up" || newStatus === "ready for shipping") && oldStatus !== "processing")
        return false;
    // completed means the customer has picked it up or it has been shipped.
    if(newStatus === "completed" && (oldStatus !== "ready for pick up" || oldStatus !== "ready for shipping"))
        return false;

    return true;
}