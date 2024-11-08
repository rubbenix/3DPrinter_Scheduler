var express = require('express');
var router = express.Router();
const orderController = require("../controllers/orderController.js")

router.get('/', function(req, res, next) {
  res.json(orderController.getAllOrders());
});

router.get("/:id", (req, res) => {
  try {
    res.json(orderController.findById(req.params.id));
  } catch(err) {
    res.status(err.code).json({msg: err.message});
  }
});

router.patch("/:id", (req, res, next) => {

  try {
    res.json(orderController.patchOrder(req.params.id, req.body));
  } catch(err) {
    res.status(err.code).json({msg: err.message});
  }
});

router.post("/", (req, res, next) => {
  try {
    res.status(201).json(orderController.postOrder(req.body))
  } catch(err) {
    res.status(err.code).json({msg: err.message});
  }
});

module.exports = router;
