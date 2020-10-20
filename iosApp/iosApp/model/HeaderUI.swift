//
//  HeaderUI.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI

struct HeaderUI: Identifiable {
    let id: Int
    let title: String
    init(id: Int, title: String) {
        self.id = id
        self.title = title
    }
}
