//
//  HomeUI.swift
//  iosApp
//
//  Created by Coder on 10/14/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

struct HomeUI: Identifiable {
    let id = UUID()
    let home: Any
    
    init(home: Any) {
        self.home = home
    }
}
