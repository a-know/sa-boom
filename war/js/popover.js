$(function () {
  $("a[rel=popover]")
    .popover({
             offset: 20,
             placement: 'above'
             })
    .click(function(e) {
                        e.preventDefault()
                        })
  })